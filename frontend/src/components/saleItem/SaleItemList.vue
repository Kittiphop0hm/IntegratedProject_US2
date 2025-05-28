<script setup>
import { ref, onMounted, computed } from "vue";
import { getItems, deleteItemById } from "../../libs/fetchUtil.js";
import ListTableModel from "../model/ListTableModel.vue";
import Navbar from "../../views/Navbar.vue";
import Search from "../Search.vue";
import SaleItemDelete from "../model/DeletePopupModel.vue";
import { useRouter, useRoute } from "vue-router";
import DeletePopupModel from "../model/DeletePopupModel.vue";
import AlertMessageModel from "../model/AlertMessageModel.vue";
import FilterSaleItem from "./FilterSortSaleItem.vue";
import SortSaleItemByBrandname from "./SortSaleItemByBrandName.vue";

const route = useRoute();
const saleItems = ref([]);
const isDelete = ref(false);
const router = useRouter();

onMounted(async () => {
  try {
    saleItems.value = await getItems(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`
    );
    console.log(saleItems.value);
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

const isSuccess = ref(
  Boolean(route.query.alertAdd || route.query.alertDelete) &&
    !route.query.alert404
);

const deleteId = ref(null);
const handledelete = (id) => {
  isDelete.value = true;
  deleteId.value = id;
  // selectedBrandId.value = id;
  // selectedBrandName.value = brandName;
  // console.log("selectedBrandId.value", selectedBrandId.value);
};
// const formattedPrice = computed(() =>
//   saleItems.value.price != null ? saleItems.value.price.toLocaleString() : "-"
// );
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
      <AlertMessageModel :isSuccess="isSuccess">
        <template #message>
          <p class="itbms-message" v-show="isSuccess === true">
            The sale item has been
            <span class="text-green-400">
              {{
                route.query.alertAdd ? "successfully added." : "deleted."
              }}</span
            >
          </p>
          <p class="itbms-message" v-show="isSuccess === false">
            The requested sale item does not exist.
          </p>
        </template>
      </AlertMessageModel>
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

      <router-link :to="{ name: 'BrandManager' }">
        <button
          class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 mb-5 mt-5 itbms-manage-brand"
        >
          Manage Brand
        </button>
      </router-link>
    </div>

    <div>
      <div
        v-show="saleItems.length === 0"
        class="p-5 w-full border flex justify-center items-center"
      >
        no sale item
      </div>
      <ListTableModel :items="saleItems">
        <template #listItem="{ yourItem }">
          <td class="border px-2 py-1 itbms-id">
            {{ yourItem.id ?? "-" }}
          </td>
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
              class="bg-yellow-400 text-white px-2 py-1 rounded mr-2 itbms-edit-button"
            >
              Edit
            </router-link>
            <button
              class="bg-red-500 text-white px-2 py-1 rounded itbms-delete-button"
              @click="handledelete(yourItem.id)"
            >
              Delete
            </button>
          </td>
        </template>
      </ListTableModel>
      <DeletePopupModel
        v-if="isDelete"
        @cancel-delete="cancelDelete"
        @delete-sale-item="deleteSaleItem(deleteId)"
      >
        <template #message>
          <span class="itbms-message font-semibold">
            Do you want to delete this sale item?
          </span>
        </template>
      </DeletePopupModel>
    </div>
  </div>
</template>
