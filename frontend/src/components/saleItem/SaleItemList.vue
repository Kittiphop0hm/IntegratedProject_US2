<script setup>
import { ref, onMounted } from "vue";
import { getItems, deleteItemById } from "../../libs/fetchUtil.js";
import ListTableModel from "../model/ListTableModel.vue";
import Navbar from "../../views/Navbar.vue";
import Search from "../Search.vue";
import SaleItemDelete from "./SaleItemDelete.vue";
import { useRouter, useRoute } from "vue-router";
const route = useRoute();
const saleItems = ref([]);
const isDelete = ref(false);
const router = useRouter();

// const props = defineProps({
//   typePopup: {
//     validator(value) {
//       return ["sale-items", "brands"].includes(value);
//     },
//     type: String,
//     default:'Sale-items'
//   },
// });

onMounted(async () => {
  try {
    saleItems.value = await getItems(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`
    );
  } catch (err) {
    console.error("Error fetching sale items:", err);
  }
});

const cancelDelete = () => {
  isDelete.value = false;
};

// const deleteStatus = ref(0)
const deleteSaleItem = async (id) => {
  try {
    const deleteStatus = await deleteItemById(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
      id
    );
    if (deleteStatus === 204) {
      const removeIndex = saleItems.value.findIndex((item) => item.id === id);
      saleItems.value.splice(removeIndex, 1);
      isDelete.value = false;
      router.push({ name: "SaleItemList", query: { alertDelete: "true" } });
    }
    if (deleteStatus === 404) {
      router.push({ name: "SaleItemList", query: { alert404: "true" } });
    }
  } catch (err) {
    console.error(err);
  }
};
function savePreviousPath() {
  const previousPath = route.fullPath;
  localStorage.setItem("previousPath", previousPath);
}
</script>

<template>
  <Navbar />
  <Search />

  <div class="p-10 pt-0">
    <div
      v-show="
        route.query.alertAdd || route.query.alertDelete || route.query.alert404
      "
      class="p-10 pb-0"
    >
      <div class="bg-black/20 shadow-xl rounded px-8 pt-6 pb-8">
        <div v-show="route.query.alertAdd || route.query.alertDelete">
          <h1 class="text-2xl text-green-400">Successfully</h1>
          <br />
          <p class="itbms-message">
            The sale item has been
            <span class="text-green-400">
              {{
                route.query.alertAdd ? "successfully added." : "deleted."
              }}</span
            >
          </p>
        </div>
        <div v-show="route.query.alert404">
          <h1 class="text-2xl text-red-400">Error</h1>
          <br />
          <p class="itbms-message">The requested sale item does not exist.</p>
        </div>
      </div>
    </div>
    

    <div class="flex justify-between items-center px-10">
      <router-link :to="{ name: 'SaleItemAdd' }">
        <button
          @click="savePreviousPath"
          class="itbms-sale-item-add px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 mb-5 mt-5"
        >
          Add Sale Item
        </button>
      </router-link>

      <router-link :to="{ name: 'BrandList' }">
        <button
          class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 mb-5 mt-5"
        >
          Manage Brand
        </button>
      </router-link>
    </div>


    <ListTableModel :items="saleItems">
      <template #listItem="{ yourItem }">
        <td class="border px-2 py-1 itbms-id">{{ yourItem.id ?? "-" }}</td>
        <td class="border px-2 py-1 itbms-brand">
          {{ yourItem.brandName ?? "-" }}
        </td>
        <td class="border px-2 py-1 itbms-model">
          {{ yourItem.model ?? "-" }}
        </td>
        <td class="border px-2 py-1 itbms-ramGb">
          {{ yourItem.ramGb ?? "-" }}
        </td>
        <td class="border px-2 py-1 itbms-storageGb">
          {{ yourItem.storageGb ?? "-" }}
        </td>
        <td class="border px-2 py-1 itbms-color">
          {{ yourItem.color ?? "-" }}
        </td>
        <td class="border px-2 py-1 itbms-price">
          {{ yourItem.price.toLocaleString() ?? "-" }}
        </td>
      </template>
      <template #action="{ yourItem }">
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
            @click="isDelete = true"
          >
            Delete
          </button>
        </td>
        <SaleItemDelete
          v-show="isDelete"
          @cancel-delete="cancelDelete"
          @delete-sale-item="deleteSaleItem(yourItem.id)"
        />
      </template>
    </ListTableModel>
  </div>
</template>
