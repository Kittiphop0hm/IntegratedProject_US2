<script setup>
import { ref, onMounted } from "vue";
import { getItems } from "../../libs/fetchUtil.js";

const saleItems = ref([]);

onMounted(async () => {
  try {
    saleItems.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`);
  } catch (err) {
    console.error("Error fetching sale items:", err);
  }
});
</script>

<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold mb-4">Sale Items List</h1>

    <table class="w-full border border-gray-300 text-sm">
      <thead class="bg-gray-100">
        <tr>
          <th class="border px-2 py-1">Brand</th>
          <th class="border px-2 py-1">Model</th>
          <th class="border px-2 py-1">RAM (GB)</th>
          <th class="border px-2 py-1">Storage (GB)</th>
          <th class="border px-2 py-1">Screen Size</th>
          <th class="border px-2 py-1">Color</th>
          <th class="border px-2 py-1">Price</th>
          <th class="border px-2 py-1">Quantity</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="item in saleItems"
          :key="item.id"
          class="hover:bg-gray-50"
        >
          <td class="border px-2 py-1 itbms-brand">{{ item.brand.name }}</td>
          <td class="border px-2 py-1 itbms-model">{{ item.model }}</td>
          <td class="border px-2 py-1 itbms-ramGb">{{ item.ramGb ?? '-' }}</td>
          <td class="border px-2 py-1 itbms-storageGb">{{ item.storageGb ?? '-' }}</td>
          <td class="border px-2 py-1 itbms-screenSizeInch">{{ item.screenSizeInch ?? '-' }}</td>
          <td class="border px-2 py-1 itbms-color">{{ item.color ?? '-' }}</td>
          <td class="border px-2 py-1 itbms-price">{{ item.price.toLocaleString('en-US') }} ฿</td>
          <td class="border px-2 py-1 itbms-quantity">{{ item.quantity }}</td>
        </tr>
        <tr v-if="saleItems.length === 0">
          <td colspan="8" class="text-center text-gray-400 py-4">No items found.</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
