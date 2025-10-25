<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, onMounted, computed } from "vue";
import {
  getItems,
  deleteItemById,
  getItemsWithToken,
} from "../../libs/fetchUtil";
import { decodeJWT } from "@/libs/decodeJWT.js";
import Navbar from "../../views/Navbar.vue";
import SaleItemDetailModel from "../model/SaleItemDetailModel.vue";
import DeletePopupModel from "../model/DeletePopupModel.vue";
import AlertMessageModel from "../model/AlertMessageModel.vue";
const accessToken = sessionStorage.getItem("accessToken");
const getUser = decodeJWT(accessToken);
import { useUserStore } from "@/stores/users";
const userStore = useUserStore();
import { useCartStore } from "@/stores/carts.js";
const cartStore = useCartStore();
const route = useRoute();
const router = useRouter();
const item = ref([]);
const isDelete = ref(false);
// const quantityInCart = ref(1);
onMounted(async () => {
  try {
    const data = await getItems(
      `${import.meta.env.VITE_APP_URL}/v2/sale-items/${route.params.id}/sellers`
    );
    if (data.status === 404) {
      router.push({ name: "SaleItemHome", query: { alert404: "true" } });
    } else {
      item.value = data;
    }
  } catch (err) {
    console.error(err);
  }
});

const formattedPrice = computed(() =>
  item.value?.price != null ? item.value.price.toLocaleString() : "-"
);

const cancelDelete = () => {
  isDelete.value = false;
};

// const deleteStatus = ref(0)
const deleteSaleItem = async () => {
  try {
    const deleteStatus = await deleteItemById(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
      item.value.id
    );
    if (deleteStatus === 204) {
      isDelete.value = false;
      router.push({ name: "SaleItemHome", query: { alertDelete: "true" } });
    }
    if (deleteStatus === 404) {
      router.push({ name: "SaleItemHome", query: { alert404: "true" } });
    }
  } catch (err) {
    console.error(err);
  }
};
console.log(route.query.alert);
console.log(typeof route.query.alert);
console.log("typeof Boolean(route.query.alert)");
console.log(typeof Boolean(route.query.alert));

const idParam = parseInt(route.params.id);
// console.log(id);
const isShowAlertMessageModel = ref(false);
const messageAlert = ref("");
const isSuccess = ref(Boolean(route.query.alert));
const quantityInCart = ref(1);

const displayQuantityInCart = (qty) => {
  quantityInCart.value = qty;
};
const checkRole = () => {
  if (userStore.role === "") {
    console.log("no role stupid 250 SaleitemMnaager");
    router.push({ name: "Login" });
    return;
  }
  if (item.value.seller.id === userStore.id) {
    isShowAlertMessageModel.value = true;
    isSuccess.value = false;
    messageAlert.value = "You cannot add your own item to the cart.";
  } else {
    console.log(item.value)
    let copyItem = JSON.parse(JSON.stringify(item.value));
    copyItem.quantityEach = quantityInCart.value;
    const result = cartStore.isMaxQtyInStock( copyItem , quantityInCart.value);
    console.log("Result check max qty in stock: ", result);
    if(typeof result === 'string') {
      isShowAlertMessageModel.value = true;
      isSuccess.value = false;
      messageAlert.value = result;
    }
    else {
      console.log("Error mai")
      cartStore.pushInCart(copyItem , quantityInCart.value);
      alert("Ok Herbal")
    }

  }
};
</script>

<template>
  <Navbar />
  <div
    v-show="route.query.alert === 'true' || isShowAlertMessageModel === true"
    class="p-10 pb-0"
  >
    <AlertMessageModel :isSuccess="isSuccess">
      <template #message>
        <p v-show="isSuccess === true">
          The sale item has been <span class="text-green-400">updated.</span>
        </p>
        <p v-show="isSuccess === false">
          {{ messageAlert }}
        </p>
      </template>
    </AlertMessageModel>
  </div>
  <SaleItemDetailModel :saleId="idParam" @addToCart="displayQuantityInCart"  :saleItemEach="item">
    <template #path>
      <span class="itbms-model font-semibold">{{ item.model }}</span>
      <span class="itbms-ramGb font-semibold ml-1"
        >{{ item.ramGb }}<span class="itbms-ramGb-unit">/GB </span></span
      >
      <span class="itbms-color font-semibold">{{
        item.color ? item.color : "-"
      }}</span>
    </template>

    <template #brand>
      <span class="itbms-brand">{{ item.brandName }}</span>
    </template>
    <template #model>
      <span class="itbms-model">{{ item.model }}</span>
    </template>
    <template #price>
      <span class="itbms-price">Price: ฿ {{ formattedPrice }} Baht</span>
    </template>
    <template #desc>
      <span class="itbms-description">{{ item.description }} </span>
    </template>
    <template #ram>
      <span class="itbms-ramGb">{{ item.ramGb ? item.ramGb : "-" }}</span>
      <span class="itbms-ramGb-unit"> GB</span>
    </template>
    <template #screen>
      <span class="itbms-screenSizeInch">{{
        item.screenSizeInch ? item.screenSizeInch : "-"
      }}</span>
      <span class="itbms-screenSizeInch-unit">Inches</span></template
    >
    <template #storage>
      <span class="itbms-storageGb">{{
        item.storageGb ? item.storageGb : "-"
      }}</span>
      <span class="itbms-storageGb-unit">GB</span></template
    >
    <template #color>
      <span class="itbms-color">{{ item.color ? item.color : "-" }}</span>
    </template>
    <template #quantity>
      <span class="itbms-quantity">{{ item.quantity }}</span>
      <span class="itbms-quantity-unit">units</span></template
    >
    <template #button1>
      <router-link :to="{ name: 'SaleItemEdit' }">
        <span
          class="itbms-edit-button"
        >
          Edit
        </span>
      </router-link>
    </template>
    <template #button2>
      <span @click="isDelete = !isDelete" class="itbms-delete-button"
        >Delete</span
      >
    </template>
    <!-- <template #buttonMinus >  -->
    <!-- <span class="w-full border" @click="decreaseQty">-</span>  -->
    <!-- </template> -->
    <template #quantityInCart> {{ quantityInCart }} </template>
    <!-- <template #buttonPlus>
      <span @click="quantityInCart++">+</span>
    </template> -->
    <template #button3>
      <span @click="checkRole" class="itbms-add-to-cart-button">
        <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 24 24"><path fill="#ffffff" d="M17 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2M1 2v2h2l3.6 7.59l-1.36 2.45c-.15.28-.24.61-.24.96a2 2 0 0 0 2 2h12v-2H7.42a.25.25 0 0 1-.25-.25c0-.05.01-.09.03-.12L8.1 13h7.45c.75 0 1.41-.42 1.75-1.03l3.58-6.47c.07-.16.12-.33.12-.5a1 1 0 0 0-1-1H5.21l-.94-2M7 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2Z"/></svg></span
      >
    </template>
  </SaleItemDetailModel>
  <DeletePopupModel
    v-show="isDelete"
    @cancel-delete="cancelDelete"
    @delete-sale-item="deleteSaleItem"
  >
    <template #message>
      <span class="itbms-message font-semibold">
        Do you want to delete this sale item?
      </span>
    </template>
  </DeletePopupModel>
</template>
