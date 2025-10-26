<script setup>
import { ref, watchEffect, computed, watch } from "vue";
import Navbar from "../../views/Navbar.vue";
import Search from "../Search.vue";
import { useCartStore } from "@/stores/carts.js";
import { useUserStore } from "@/stores/users";
import AlertMessageModel from "../model/AlertMessageModel.vue";
import DeletePopupModel from "../model/DeletePopupModel.vue";
import { addItemWithToken, getItemById } from "../../libs/fetchUtil.js";
import { decodeJWT } from "@/libs/decodeJWT";
import router from "@/router";
const userStore = useUserStore();
const cartStore = useCartStore();
const arrayCartItems = ref(cartStore.cartObj);
console.log(arrayCartItems.value);
const address = ref(localStorage.getItem("address") || null);
const note = ref(localStorage.getItem("note") || null);

watch(address, (newVal) => {
  localStorage.setItem("address", newVal);
});

watch(note, (newVal) => {
  localStorage.setItem("note", newVal);
});

const selectAllCheck = ref(false);
const accessToken = localStorage.getItem("accessToken");
// watch(selectAllCheck, (newVal) => {
//   // sellerCheck.value = newVal
//   arrayCartItems.value.forEach( obj => {
//     obj.checked = newVal
//     obj.items.forEach( item => { item.checked = newVal })
//   })
// })
// const arrCheckedTrue = computed( () =>
//   arrayCartItems.value.filter(seller => seller.checked || seller.items.some(item => item.checked))
// )
// const totalItems = ref(arrCheckedTrue.value.reduce((acc,cur)))

const totalQuantity = computed(() => {
  return arrayCartItems.value.reduce((accAll, seller) => {
    const totalEachSeller = seller.items
      .filter((item) => item.checked)
      .reduce((accEach, item) => accEach + item.quantity, 0);
    return accAll + totalEachSeller;
  }, 0);
});

const totalPrice = computed(() => {
  return arrayCartItems.value.reduce((accAll, seller) => {
    const totalEachSeller = seller.items
      .filter((item) => item.checked)
      .reduce((accEach, item) => accEach + item.price * item.quantity, 0);
    return accAll + totalEachSeller;
  }, 0);
});

// const totalPrice = computed(() => {
//   return arrayCartItems.value.reduce((accAll, seller) => {
//     console.log(seller);
//     const totalEachSeller = seller.items.reduce((accEach, item) => {
//       // console.log(typeof item.price);
//       // console.log(typeof accEach);
//       return accEach + item.price * item.quantity;
//     }, 0);
//     return totalEachSeller + accAll;
//   }, 0);
// });

watchEffect(() => {
  console.log(totalQuantity.value);
  console.log(address.value)
});

function checkSelectAll() {
  arrayCartItems.value.forEach((obj) => {
    obj.checked = selectAllCheck.value;
    obj.items.forEach((item) => {
      item.checked = selectAllCheck.value;
    });
  });
}

function checkSeller(seller) {
  console.log(seller);
  seller.items.forEach((item) => {
    item.checked = seller.checked;
  });
  selectAllCheck.value = arrayCartItems.value.every((seller) => seller.checked);
}

function checkItem(seller) {
  console.log(seller);
  seller.checked = seller.items.every((item) => item.checked);
  // seller.forEach(item => {
  //   seller.checked =
  // } )
  selectAllCheck.value = arrayCartItems.value.every((seller) => seller.checked);
}
const isShowAlertMessageModel = ref(false);
const messageAlert = ref("");
const isSuccess = ref();
const isWarning = ref();
const itemDelete = ref();
function changeQty(item, value) {
  const newQty = item.quantity + value;
  
  console.log("New Qty: ", newQty);
  if (newQty < 1) {
    itemDelete.value = item;
    isDelete.value = true;
    return;
  }

  const result = cartStore.isMaxQtyInStock(item, newQty, "saleItemCart");
  console.log("Result check max qty in stock: ", result);
  if (typeof result === "string") {
    isShowAlertMessageModel.value = true;
    isSuccess.value = false;
    isWarning.value = true;
    messageAlert.value = result;
    // return;
  }
  item.quantity = newQty;
  cartStore.cartQuantity = cartStore.cartQuantity + value;
}

const isDelete = ref(false);
const cancelDelete = () => {
  isDelete.value = false;
};

const deleteItem = (itemDelete) => {
  cartStore.cartObj = arrayCartItems.value;
  arrayCartItems.value = arrayCartItems.value.map((seller) => ({
    ...seller,
    items: seller.items.filter(
      (item) => item.saleItemId !== itemDelete.saleItemId
    ),
  }));
  cartStore.cartObj = arrayCartItems.value;
  cartStore.cartQuantity -= 1;
  isDelete.value = false;
};

async function changeFormattedObject() {
  console.log("Change Formatted Object");
  console.log(arrayCartItems.value);
  for(const seller of arrayCartItems.value) {
    console.log(seller.sellerName);
    for(const item of seller.items) {
      console.log("Checking item: ", item.saleItemId, item.model);
      console.log("typeof saleItemId:" + typeof item.saleItemId);
      const data = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, item.saleItemId)
      console.log("Qty in stock: ", item.qtyInstock);
      console.log("Data from API: ", data);
      console.log(data)
      if(data !== item.qtyInstock) { 
        item.qtyInstock = data.quantity;
      }
      console.log("Updated Qty in stock: ", item.qtyInstock);
    }
  }
  const orderData = arrayCartItems.value
    .filter(
      (seller) => seller.checked || seller.items.some((item) => item.checked)
    )
    .map((seller) => ({
      buyerId: userStore.id,
      sellerId: seller.sellerId,
      orderDate: new Date().toISOString(),
      shippingAddress: address.value,
      orderNote: note.value,
      orderStatus: seller.items
        .filter((item) => item.checked)
        .some((item) => item.quantity > item.qtyInstock)
        ? "CANCELLED"
        : "COMPLETED",
      // ...seller,
      isNewOrder: true,
      orderItems: seller.items
        .filter((item) => item.checked)
        .map((item) => (
        {
          saleItemId: item.saleItemId,
          price: item.price,
          quantity: item.quantity,
          description: item.brandName + " " + item.model + " " + "(" + item.storageGb + "," + item.color + ")"
        })),
    }));

  return orderData;
}

async function placeOrder() {
  const orderData = await changeFormattedObject();
  const decode = decodeJWT(accessToken);
  console.log(decode);
  
  console.log(cartStore.cartObj)
  console.log(orderData);
  console.log(JSON.stringify(orderData, null, 2));
  const item = await addItemWithToken(
    `${import.meta.env.VITE_APP_URL}/v2/orders`,
    orderData,
    accessToken
  );

  const listSaleItemIds = orderData.flatMap(seller => seller.orderItems.map(item => item.saleItemId));
  console.log(listSaleItemIds);
  cartStore.cartObj = cartStore.cartObj.map(seller => ({
    ...seller,
    items: seller.items.filter(item => !listSaleItemIds.includes(item.saleItemId))
  })).filter(seller => seller.items.length > 0);
  cartStore.cartQuantity = cartStore.cartObj.reduce((acc, seller) => {
    const totalItems = seller.items.reduce((accItems, item) => accItems + item.quantity, 0);
    return acc + totalItems;
    // acc + seller.items.length 
  }, 0);
  arrayCartItems.value = cartStore.cartObj;
  // cartStore.cartObj.filter( () =>  )
  // cartStore.clearCart();
  // arrayCartItems.value = cartStore.cartObj;
  isShowAlertMessageModel.value = true;
  isSuccess.value = true;
  console.log(item);
  setTimeout(() => {
    router.push({ name: "SaleItemHome" });
  }, 3000);
}
</script>

<template>
  <Navbar />
  <Search />

  <div>
    <DeletePopupModel
      v-if="isDelete"
      @cancel-delete="cancelDelete"
      @delete-sale-item="deleteItem(itemDelete)"
    >
      <template #message>
        <span class="font-semibold text-[#523F31]">
          Do you want to delete this sale item?
        </span>
      </template>
    </DeletePopupModel>
  </div>

  <div v-show="isShowAlertMessageModel" class="container my-5">
    <AlertMessageModel :isWarning="isWarning" :isSuccess="isSuccess">
      <template #message>
        <p v-show="isSuccess" class="text-[#796254] font-semibold">
          Your order has been successfully <span class="text-[#9D8A7C]">processed</span>
        </p>
        <p v-show="isWarning" class="text-red-500 font-semibold">
          {{ messageAlert }}
        </p>
      </template>
    </AlertMessageModel>
  </div>

  <div class="container flex gap-6 ">
    <!-- Cart Items -->
    <div class="w-3/5 border border-[#796254] rounded-lg p-6 bg-[#ebe8e8]">
      <h1 class="text-2xl font-bold text-[#523F31] mb-4">Shopping Cart</h1>
      <div class="flex items-center mb-4 gap-2">
        <input
          type="checkbox"
          v-model="selectAllCheck"
          @change="checkSelectAll"
          class="w-5 h-5 accent-[#9D8A7C]"
        />
        <span class="text-[#523F31] font-semibold">Select All</span>
      </div>

      <div v-for="(obj, index) in arrayCartItems" :key="index" class="mb-6">
        <div v-if="obj.items.length > 0" class="flex items-center gap-2 mb-2">
          <input
            type="checkbox"
            v-model="obj.checked"
            @change="checkSeller(obj)"
            class="w-5 h-5 accent-[#9D8A7C]"
          />
          <span class="font-semibold text-[#523F31]">{{ obj.sellerName }}</span>
        </div>

        <div
          v-for="(item, index) in obj.items"
          :key="index"
          class="flex items-center gap-3 border border-[#796254] p-3 rounded-lg mb-3 bg-white"
        >
          <input
            type="checkbox"
            v-model="item.checked"
            @change="checkItem(obj)"
            class="w-5 h-5 accent-[#9D8A7C]"
          />
          <img
            src="/images/iPhone14ProMax.jpg"
            alt="phone image"
            class="w-24 h-24 object-cover rounded-md"
          />
          <div class="flex-1 text-[#523F31] font-medium">
            {{ item.brandName }} {{ item.model }} 
            ({{ item.storageGb }}GB, {{ item.color }})
          </div>
          <div class="flex items-center gap-2">
            <button
              @click="changeQty(item, -1)"
              class="px-3 py-1 bg-[#9D8A7C] text-white rounded hover:bg-[#796254]"
            >
              -
            </button>
            <span class="w-6 text-center">{{ item.quantity }}</span>
            <button
              @click="changeQty(item, 1)"
              class="px-3 py-1 bg-[#9D8A7C] text-white rounded hover:bg-[#796254]"
            >
              +
            </button>
          </div>
          <div class="font-semibold text-[#523F31]">
            {{ (item.price * item.quantity).toLocaleString() }} ฿
          </div>
        </div>
      </div>
    </div>

    <!-- Cart Summary -->
    <div class="w-2/5 border border-[#796254] rounded-lg p-6 bg-[#ebe8e8]">
      <h1 class="text-3xl font-bold text-[#523F31] mb-4">Cart Summary</h1>
      <hr class="border-[#796254] mb-4" />

      <h2 class="text-[#523F31] font-semibold">Ship To</h2>
      <textarea
        v-model="address"
        class="w-full p-3 border border-[#796254] rounded-md mb-4 text-[#523F31]"
        placeholder="พิมพ์ข้อความที่นี่..."
      ></textarea>

      <h2 class="text-[#523F31] font-semibold">Note</h2>
      <textarea
        v-model="note"
        class="w-full p-3 border border-[#796254] rounded-md mb-4 text-[#523F31]"
        placeholder="Additional instructions or requests"
      ></textarea>

      <div class="mb-2 flex justify-between text-[#523F31] font-semibold">
        <span>Total items:</span>
        <span>{{ totalQuantity }}</span>
      </div>
      <div class="mb-4 flex justify-between text-[#523F31] font-semibold">
        <span>Total price:</span>
        <span>{{ totalPrice.toLocaleString() }} ฿</span>
      </div>

      <button
        @click="placeOrder"
        :disabled="totalQuantity === 0 || !address"
        class="w-full py-2 px-4 rounded-lg font-semibold text-white shadow-md transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500 disabled:cursor-not-allowed"
        :class="totalQuantity > 0 && address ? 'bg-[#9D8A7C] hover:bg-[#796254]' : ''"
      >
        Place Order
      </button>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 50px auto;
}
</style>