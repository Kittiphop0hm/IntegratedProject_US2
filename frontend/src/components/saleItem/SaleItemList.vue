<script setup>
import { ref, onMounted, computed , watch} from "vue";
import { getItems, deleteItemById ,getItemsWithToken } from "../../libs/fetchUtil.js";
import ListTableModel from "../model/ListTableModel.vue";
import Navbar from "../../views/Navbar.vue";
import Search from "../Search.vue";
import SaleItemDelete from "../model/DeletePopupModel.vue";
import { useRouter, useRoute } from "vue-router";
import DeletePopupModel from "../model/DeletePopupModel.vue";
import AlertMessageModel from "../model/AlertMessageModel.vue";
import FilterSaleItem from "./FilterSortSaleItem.vue";
import SortSaleItemByBrandname from "./SortSaleItemByBrandName.vue";
import {decodeJWT} from "@/libs/decodeJWT.js";
import { useCountNewOrder } from "@/stores/countNewOrder.js";
let pageSizeWatchInitialized = false;
const route = useRoute();
const saleItems = ref([]);
const isDelete = ref(false);
const router = useRouter();
const accessToken = localStorage.getItem('accessToken')
const getUser = decodeJWT(accessToken)
const pageObj = ref({});
const pageSize = ref();
const pageNumber = ref();
const pageNumberSession = localStorage.getItem("pageNumber");
const pageSizeSession = localStorage.getItem("pageSize");
const {fetchCountNewOrder} = useCountNewOrder()
watch([pageSize, pageNumber], () => {
  localStorage.setItem("pageSize", pageSize.value);
  localStorage.setItem("pageNumber", pageNumber.value);
  fetchData();
});
watch(pageSize, () => {
  if (!pageSizeWatchInitialized) {
    pageSizeWatchInitialized = true;
    return;
  }
  pageNumber.value = 0;
});

const fetchData = async () => {
  try {
       const res = await getItemsWithToken(
// `${import.meta.env.VITE_APP_URL}/v2/seller/${getUser.id}/sale-items?page=${pageNumber}&size=${pageSize}` ,
`${import.meta.env.VITE_APP_URL}/v2/sellers/${getUser.id}/sale-items?page=${pageNumber.value}&size=${pageSize.value}` ,accessToken)
      pageObj.value = res
      saleItems.value = res.content
     console.log(pageObj.value);
  } catch (err) {
    console.log(err);
  }
};
onMounted(async () => {
  if(!accessToken)router.push({name:'Login'})
  try {
      pageSize.value = pageSizeSession ? Number(pageSizeSession) : 10;
      pageNumber.value = pageNumberSession ? Number(pageNumberSession) : 0;
      await fetchCountNewOrder()
    // saleItems.value = await getItems(
    //   `${import.meta.env.VITE_APP_URL}/v1/sale-items`
    // );

  } catch (err) {
    console.error("Error fetching sale items:", err);
  }
});

const fecthItemFromPage = async(index) => {
  pageNumber.value = index - 1;
  await fetchData();
};
const cancelDelete = () => {
  isDelete.value = false;
};

const deleteSaleItem = async (id) => {
  try {
    const deleteStatus = await deleteItemById(
      `${import.meta.env.VITE_APP_URL}/v2/sale-items`,
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

const isSuccess = ref(
  Boolean(route.query.alertAdd || route.query.alertDelete) &&
    !route.query.alert404
);

const deleteId = ref(null);
const handledelete = (id) => {
  isDelete.value = true;
  deleteId.value = id;
};


const isFirst = computed(() => {
  return pageNumber.value === 0;
});

const isLast = computed(() => {
  return pageNumber.value === pageObj.value.totalPages - 1;
});

const computedPageNumberArr = computed(() => {
  const arr = [];
  let maxDisplay = 10;
  console.log("pageNumber.value:", pageNumber.value);
  console.log("pageObj.value.totalPages:", pageObj.value.totalPages);
  for (let i = 0; i < pageObj.value.totalPages; i++) {
    arr.push(i + 1);
  }
  if (arr.length > maxDisplay) {
    let difference = arr.length - pageNumber.value;
    if (difference > maxDisplay) {
      arr.splice(
        arr.length - (difference - maxDisplay),
        Math.abs(difference - maxDisplay)
      );
      if (pageNumber.value !== 0) {
        arr.splice(0, difference - maxDisplay);
      }
    } else {
      while (arr.length !== maxDisplay) {
        arr.shift();
      }
    }
  }
  console.log("133")
  console.log(arr)
  return arr;
});
</script>

<template>
  <Navbar />
  <Search />
  <div class="p-10 pt-0 mt-10">
    <div
      v-show="
        route.query.alertAdd || route.query.alertDelete || route.query.alert404
      "
      class="p-10 pb-0">
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
        <div>
      <span>
        <select
          class="w-fit select bg-[#523F31] text-white font-semibold"
          v-model.number="pageSize"
        >
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
        </select>
        </span
      >
    </div>
    <div class="flex justify-between items-center">
      <router-link :to="{ name: 'SaleItemAdd' }">
        <button
          @click="savePreviousPath"
          class="flex justify-center items-center gap-x-2 itbms-sale-item-add px-4 py-2 bg-[#523F31] text-white font-semibold cursor-pointer rounded-md hover:opacity-90"
        >
          <p>Add SaleItem</p>
          <svg xmlns="http://www.w3.org/2000/svg" width="23" height="23" viewBox="0 0 24 24"><g fill="none"><circle cx="12" cy="12" r="9" fill="#ffffff"/><path stroke="#000000" stroke-linecap="square" stroke-linejoin="round" stroke-width="1.2" d="M12 8v8m4-4H8"/></g></svg>
        </button>
      </router-link>

      <router-link :to="{ name: 'BrandManager' }">
        <button
          class="px-4 py-2 bg-[#523F31] font-semibold  text-white rounded-md hover:opacity-80 cursor-pointer mb-5 mt-5 itbms-manage-brand"
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
              class="bg-[#796254] text-white text-center px-3 py-1 rounded mr-2 itbms-edit-button cursor-pointer hover:opacity-80"
            >
              Edit
            </router-link>
            <button
              class="bg-[#9D8A7C] text-white px-3 py-1 rounded itbms-delete-button cursor-pointer hover:opacity-80"
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
      <div class="mb-5"></div>
    </div class="">
      <div v-show="pageObj.totalPages > 1">
    <button
      class="itbms-page-first cursor-pointer bg-[#796254] text-white inset-shadow-xs shadow-black shadow-[0_4px_6px_rgba(82,63,90,1)] pl-5 pr-5 pt-3 pb-3 rounded-l-lg"
      @click="pageNumber = 0"
      :class="isFirst ? 'opacity-60' : 'bg-[#796254]'"
      :disabled="isFirst"
    >
      First
    </button>
    <button
      class="itbms-page-prev cursor-pointer bg-[#796254] text-white inset-shadow-xs shadow-black shadow-[0_4px_6px_rgba(82,63,90,1)] pl-5 pr-5 pt-3 pb-3"
      @click="pageNumber = pageNumber - 1"
      :class="isFirst ? 'opacity-60' : 'bg-[#796254]'"
      :disabled="isFirst"
    >
      Prev
    </button>
    <span
      v-for="index in computedPageNumberArr"
      :key="index"
      @click="fecthItemFromPage(index)"
      :class="`itbms-page-${index - 1} `"
    >
      <button
        class="pl-5 pr-5 pt-3 pb-3 inset-shadow-xs text-white shadow-black font-semibold shadow-[0_4px_6px_rgba(82,63,90,1)] cursor-pointer"
        :class="
          index - 1 === pageNumber ? 'bg-[#523F31] text-white' : 'bg-[#796254]'
        "
      >
        {{ index }}
      </button>
    </span>
    <button
      class="itbms-page-next bg-[#796254] inset-shadow-xs text-white shadow-black shadow-[0_4px_6px_rgba(82,63,90,1)] pl-5 pr-5 pt-3 pb-3 cursor-pointer"
      @click="pageNumber = pageNumber + 1"
      :class="isLast ? 'opacity-60' : 'bg-[#523F31]'"
      :disabled="isLast"
    >
      Next
    </button>
    <button
      class="itbms-page-last rounded-r-lg bg-[#796254] text-white inset-shadow-xs shadow-black shadow-[0_4px_6px_rgba(82,63,90,1)] pl-5 pr-5 pt-3 pb-3 cursor-pointer"
      @click="pageNumber = pageObj.totalPages - 1"
      :class="isLast ? 'opacity-60' : 'bg-[#523F31]'"
      :disabled="isLast"
    >
      Last
    </button>
  </div>
  </div>
</template>
