<script setup>
import { ref, onMounted } from "vue";
import { getItems } from "../../libs/fetchUtil.js";
import SaleItemListTableModel from "../model/SaleItemListTableModel.vue"; 

const saleItems = ref([]);

onMounted(async () => {
  try {
    saleItems.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`);
    console.log(JSON.stringify(saleItems.value[0]))
  } catch (err) {
    console.error("Error fetching sale items:", err);
  }
});

</script>

<template>
  <div class="p-10">
    <h1 class="text-2xl font-bold mb-6">Sale Items List</h1>

    <SaleItemListTableModel :items="saleItems">
      <template #listItem="{ yourItem }">
        <tr class="itbms-row border-b">
        <td class="border px-2 py-1 itbms-id">{{ yourItem.id }}</td>
          <td class="border px-2 py-1 itbms-brand">{{ yourItem.brandName }}</td>
          <td class="border px-2 py-1 itbms-model">{{ yourItem.model }}</td>
          <td class="border px-2 py-1 itbms-ramGb">{{ yourItem.ramGb}}</td>
          <td class="border px-2 py-1 itbms-storageGb">{{ yourItem.storageGb}}</td>
          <td class="border px-2 py-1 itbms-screenSizeInch">{{ yourItem.screenSizeInch ?? '-'}}</td>
          <td class="border px-2 py-1 itbms-color">{{ yourItem.color }}</td>
          <td class="border px-2 py-1 itbms-price">{{ yourItem.price.toLocaleString()}}</td>
          <td class="border px-2 py-1 itbms-quantity">{{ yourItem.quantity }}</td>
          <td class="border px-2 py-1 text-center">
            <router-link
              :to="{ name: 'SaleItemEdit', params: { id: yourItem.id } }"
              class="bg-yellow-400 text-white px-2 py-1 rounded mr-2"
              data-testid="itbms-edit-button"
            >
              Edit
            </router-link>
            <button
              class="bg-red-500 text-white px-2 py-1 rounded"
              data-testid="itbms-delete-button"
              @click="console.log('Delete', yourItem.id)"
            >
              Delete
            </button>
          </td>
        </tr>
      </template>
    </SaleItemListTableModel>
  </div>
</template>
