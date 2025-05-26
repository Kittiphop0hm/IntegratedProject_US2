<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, onMounted, computed } from "vue";
import { getItemById, deleteItemById } from "../../libs/fetchUtil";
import Navbar from "../../views/Navbar.vue";
import SaleItemDetailModel from "../model/SaleItemDetailModel.vue";
import DeletePopupModel from "../model/DeletePopupModel.vue";
import AlertMessageModel from "../model/AlertMessageModel.vue";

const route = useRoute();
const router = useRouter();
const item = ref([]);
const isDelete = ref(false);
onMounted(async () => {
  try {
    const data = await getItemById(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
      route.params.id
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
</script>

<template>
  <Navbar />
  <div v-show="route.query.alert === 'true'" class="p-10 pb-0">
    <AlertMessageModel :isSuccess="Boolean(route.query.alert)">
      <template #message>
        The sale item has been <span class="text-green-400">updated.</span>
      </template>
    </AlertMessageModel>
  </div>
  <SaleItemDetailModel>
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
      <span class="itbms-quantity">{{
        item.quantity ? item.quantity : "-"
      }}</span>
      <span class="itbms-quantity-unit">units</span></template
    >
    <template #button1>
     <router-link :to="{ name: 'SaleItemEdit' }">
  <span class="itbms-edit-button text-white bg-blue-500 hover:bg-blue-700  px-7 py-3 rounded-xl ">
    Edit
  </span>
</router-link>

    </template>
    <template #button2>
      <span @click="isDelete = !isDelete" class="itbms-delete-button text-white"
        >Delete</span
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
        Do you want to delete this saleItems
      </span>
    </template>
  </DeletePopupModel>
</template>
