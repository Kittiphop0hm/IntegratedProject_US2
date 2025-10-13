<script setup>
import { ref, watchEffect, computed, watch } from "vue";
import Navbar from "../../views/Navbar.vue";
import Search from "../Search.vue";
import { useCartStore } from "@/stores/carts.js";
import { useUserStore } from "@/stores/users";
import AlertMessageModel from "../model/AlertMessageModel.vue";
import DeletePopupModel from "../model/DeletePopupModel.vue";
import { addItemWithToken } from "../../libs/fetchUtil.js";
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
const accessToken = sessionStorage.getItem("accessToken");
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
    messageAlert.value = result;
    return;
  }
  item.quantity = newQty;
  cartStore.cartQuantity = cartStore.cartQuantity + value;
}

const isDelete = ref(false);
const cancelDelete = () => {
  // cartStore.cartQuantity += 1;
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

function changeFormattedObject() {
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
      orderStatus: "COMPLETED",
      // ...seller,
      orderItems: seller.items
        .filter((item) => item.checked)
        .map((item) => ({
          saleItemId: item.saleItemId,
          price: item.price,
          quantity: item.quantity,
          description: item.brandName + " " + item.model + " " + "(" + item.storageGb + "," + item.color + ")"
        })),
    }));

  return orderData;
}

async function placeOrder() {
  const orderData = changeFormattedObject();
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
  if (decode.role === "SELLER") {
    const getNewOrderInLocal = localStorage.getItem("idSellerNewOrders")
    const saveIdSellerNewOrder = !getNewOrderInLocal ? [] : [...JSON.parse(getNewOrderInLocal)]
    item.data.forEach((order) => {
      saveIdSellerNewOrder.push(order.id)
    })
    localStorage.setItem("idSellerNewOrders", JSON.stringify(saveIdSellerNewOrder))
  }

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
        <span class="itbms-message font-semibold">
          Do you want to delete this sale item?
        </span>
      </template>
    </DeletePopupModel>
  </div>

  <div v-show="isShowAlertMessageModel === true" class="container">
    <AlertMessageModel :isSuccess="isSuccess">
      <template #message>
        <p v-show="isSuccess === true">
          Your order has been successfully <span class="text-green-400">processed</span>
          </p>
        <p v-show="isSuccess === false">
          {{ messageAlert }}
        </p>
      </template>
    </AlertMessageModel>
  </div>

  <div class="container border flex justify-between mt-10">
    <div class="border-red-500 border w-[60%]">
      <h1>Shopping Cart</h1>
      <div class="itbms-select-all">
        <input
          type="checkbox"
          value=""
          v-model="selectAllCheck"
          @change="checkSelectAll"
        />Select All
      </div>
      <div
        v-for="(obj, index) in arrayCartItems"
        :key="index"
        class="itbms-row my-5 p-5 flex justify-between flex-col"
      >
        <div class="mb-5" v-if="obj.items.length > 0">
          <input
            type="checkbox"
            value=""
            class="itbms-select-nickname"
            v-model="obj.checked"
            @change="checkSeller(obj)"
          />
          <span class="itbms-nickname">{{ obj.sellerName }}</span>
        </div>
        <div
          v-for="(item, index) in obj.items"
          :key="index"
          class="flex border items-center gap-3 itbms-item-row"
        >
          <input
            type="checkbox"
            value=""
            class=""
            v-model="item.checked"
            @change="checkItem(obj)"
          />
          <img
            src="/images/iPhone14ProMax.jpg"
            alt="phone image"
            class="w-20 h-25 object-cover rounded-md mb-4"
          />
          <!-- {{ Object.keys(item) }} -->
          <div class="itbms-item-description">
            {{ item.brandName }}
            {{ item.model }}
            ( {{ item.storageGb }}GB , {{ item.color }} )
          </div>
          <button
            @click="changeQty(item, -1)"
            class="itbms-dec-qty-button py-2 px-4 bg-red-400 rounded-lg"
          >
            -
          </button>
          <div class="itbms-item-quantity">{{ item.quantity }}</div>
          <button
            @click="changeQty(item, 1)"
            class="itbms-inc-qty-button py-2 px-4 bg-amber-600 rounded-lg"
          >
            +
          </button>
          <div class="itbms-item-total-price">Price: {{ (item.price * item.quantity).toLocaleString()}}</div>
        </div>
      </div>
    </div>

    <div class="border-blue-500 border w-[35%]">
      <h1 class="text-3xl">Cart Summary</h1>
      <hr />
      <h2>Shipto</h2>
      <p>
        <span class="font-bold">Address</span> [Address No, Street, Subdistrict,
        District, Province, Postal Code]
      </p>
      <textarea
        v-model="address"
        class="itbms-shipping-address border border-gray-300 rounded-md p-2 w-full h-32"
        placeholder="พิมพ์ข้อความที่นี่..."
      ></textarea>
      <p class="font-bold">Note</p>
      <textarea
        v-model="note"
        class="itbms-order-not border border-gray-300 rounded-md p-2 w-full h-32"
        placeholder="Additional instructions or requests"
      ></textarea>

      <hr />
      <div>
        <span>Total items:</span>
        <span class="itbms-total-order-items">{{ totalQuantity }}</span>
      </div>
      <div>
        <span>Total price:</span>
        <span class="itbms-total-order-price">{{ totalPrice }}</span>
      </div>
      <button
        @click="placeOrder"
        :disabled="totalQuantity === 0 || address === null"
        class="w-full bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-4 rounded-lg shadow-md transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500 disabled:cursor-not-allowed disabled:shadow-none"
      >
        Place Order
      </button>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 0 auto;
}
</style>
