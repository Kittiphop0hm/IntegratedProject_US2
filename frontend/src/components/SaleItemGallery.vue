<script setup>
import ListSaleItemModel from "./model/ListSaleItemModel.vue";
import { ref, onMounted } from "vue";
import { getItems } from "../libs/fetchUtil";

const items = ref([]);

onMounted(async () => {
  try {
    const data = await getItems(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`
    );
    items.value = data;
  } catch (err) {
    console.log(err);
  }
});
</script>

<template>
  <ListSaleItemModel :items="items">
    <template #listItem="{ yourItem }">
      <router-link
        :to="{ name: 'SaleItemDetail', params: { id: yourItem.id } }"
        class="block"
      >
        <div
          class="itbms-row border rounded-xl shadow hover:shadow-md p-4 transition duration-300 ease-in-out cursor-pointer"
        >
          <img
            src="/images/iPhone14ProMax.jpg"
            alt="phone image"
            class="w-full h-36 object-cover rounded-md mb-4"
          />
          <div class="text-sm text-gray-900 itbms-brand">
            {{ yourItem.brandName }}
          </div>
          <div class="text-lg font-semibold text-gray-800 itbms-model">
            {{ yourItem.model }}
          </div>
          <div class="text-sm mt-1">
            <span class="itbms-ramGb"> {{ yourItem.ramGb ? yourItem.ramGb : '-' }} / </span>
            <span class="itbms-storageGb"> {{ yourItem.storageGb ? yourItem.storageGb : '-' }} <span class="itbms-storageGb-unit">GB</span></span>
          </div>
          <div class="text-blue-600 text-lg font-bold mt-2 itbms-price">
            <span class="itbms-price-unit">Baht:</span
            >{{ yourItem.price.toLocaleString("en-US") }}
          </div>
        </div>
      </router-link>
    </template>
  </ListSaleItemModel>
  <div v-show="items.length === 0" class="flex justify-center mt-0">
    <h2 class="text-3xl font-bold text-black itbms-*">no sale item</h2>
  </div>
</template>
