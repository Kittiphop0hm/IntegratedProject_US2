<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, onMounted, computed } from "vue";
import { getItemById,deleteItemById } from "../../libs/fetchUtil";
import Navbar from "../../views/Navbar.vue";
import SaleItemDetailModel from "../model/SaleItemDetailModel.vue";
import SaleItemDelete from "./SaleItemDelete.vue";

const route = useRoute();
const router = useRouter();
const item = ref([]);
const isDelete = ref(false)
onMounted(async () => {
  try {
    const data = await getItemById(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
      route.params.id
    );
    if (data.status === 404) {
      alert("The requested sale item does not exist.");
      router.push("/sale-items");
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
  isDelete.value = false
}

const deleteSaleItem = async () => {
  try {
    const deleteStatus = await deleteItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, item.value.id) 
    if (deleteStatus === 204) {
      isDelete.value = false
      router.push({ name: "SaleItemHome", query: { alertDelete: "true" } });
    } 
  } catch (err) {
    console.error(err);
  }
}
</script>

<template>
  <Navbar />
  <div v-show="route.query.alert === 'true'" class=" p-10 pb-0">
    <div class="bg-black/20 shadow-xl rounded px-8 pt-6 pb-8">
      <h1 class=" text-2xl text-green-400">Successfully</h1>
      <br>
      <p>The Sale Item has been successfully <span class="text-green-400 underline">updated.</span></p>
    </div>
  </div>
  <SaleItemDetailModel>
    <template #path>
      <span class="itbms-model font-semibold">{{ item.model }}</span>
      <span class="itbms-ramGb font-semibold ml-1"
        >{{ item.ramGb }}<span class="itbms-ramGb-unit">/GB </span></span
      >
      <span class="itbms-color font-semibold">{{ item.color  ? item.color : "-" }}</span>
    </template>
    <template #brand>{{ item.brandName }}</template>
    <template #model>{{ item.model }}</template>
    <template #price>Price: ฿ {{ formattedPrice }} Baht</template>
    <template #desc>{{ item.description }}</template>
    <template #ram
      >{{ item.ramGb ? item.ramGb : "-" }}
      <span class="itbms-ramGb-unit"> GB</span></template
    >
    <template #screen
      >{{ item.screenSizeInch ? item.screenSizeInch : "-" }}
      <span class="itbms-screenSizeInch-unit">Inches</span></template
    >
    <template #storage
      >{{ item.storageGb ? item.storageGb : "-" }}
      <span class="itbms-storageGb-unit">GB</span></template
    >
    <template #color>{{ item.color ? item.color : "-" }}</template>
    <template #quantity
      >{{ item.quantity }}
      <span class="itbms-quantity-unit">units</span></template
    >
        <template #button1>
          <router-link :to="{ name: 'SaleItemEdit'  }">
            <span class="itbms-edit-button text-white">Edit</span>
          </router-link>
    </template>
    <template #button2>
      <span @click="isDelete = !isDelete" class="itbms-edit-button text-white">Delete</span>
    </template>
  </SaleItemDetailModel>
  <SaleItemDelete v-show="isDelete" @cancel-delete="cancelDelete" @delete-sale-item="deleteSaleItem"/>
</template>
