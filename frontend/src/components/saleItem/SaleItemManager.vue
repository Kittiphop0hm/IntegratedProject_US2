<script setup>
import SaleItemGallery from "../saleItem/SaleItemGallery.vue";
import { ref, onMounted, computed, watchEffect, watch } from "vue";
import { getItems } from "../../libs/fetchUtil.js";
import { useRoute } from "vue-router";
import AlertMessageModel from "../model/AlertMessageModel.vue";
import FilterSaleItem from "./FilterSortSaleItem.vue";

const route = useRoute();
const saleItem = ref([]);
const pageObj = ref({});
let pageSizeWatchInitialized = false;
const pageSize = ref();
const pageNumber = ref();

const filterBrandSession = sessionStorage.getItem("filterBrand");
const directionSession = sessionStorage.getItem("direction");
const filterBrandR = ref(filterBrandSession ? JSON.parse(filterBrandSession) : []);
const directionR = ref(directionSession ? directionSession : '');
const fieldR = ref('')
onMounted(() => {
  const pageNumberSession = sessionStorage.getItem("pageNumber");
  const pageSizeSession = sessionStorage.getItem("pageSize");
  pageSize.value = pageSizeSession ? Number(pageSizeSession) : 10;
  pageNumber.value = pageNumberSession ? Number(pageNumberSession) : 0;
});

function savePreviousPath() {
  const previousPath = route.fullPath;
  localStorage.setItem("previousPath", previousPath);
}

const isFirst = computed(() => {
  return pageNumber.value === 0;
});

const isLast = computed(() => {
  return pageNumber.value === pageObj.value.totalPages - 1;
});

const computedPageNumberArr = computed(() => {
  const arr = [];
  let maxDisplay = 10;
  for (let i = 0; i < pageObj.value.totalPages; i++) {
    arr.push(i + 1);
  }
  if (arr.length > maxDisplay) {
    let difference = Math.abs(arr.length - pageNumber.value);
    if (difference > maxDisplay) {
      arr.splice(
        arr.length - Math.abs(difference - maxDisplay),
        Math.abs(difference - maxDisplay)
      );
      if (pageNumber.value !== 0) {
        arr.splice(0, Math.abs(difference - maxDisplay));
      }
    } else {
      while (arr.length !== maxDisplay) {
        arr.shift();
      }
    }
  }
  return arr;
});

const fetchData = async () => {
  try {
    if (fieldR === "brand.name") {
      const res = await getItems(
          `${
            import.meta.env.VITE_APP_URL
          }/v2/sale-items?filterBrands=${filterBrandR.value}&sortField=brand.name&sortDirection=${directionR.value}&page=${
            pageNumber.value
          }&size=${pageSize.value}`
        );
    pageObj.value = res;
    console.log("pageObj.value:", pageObj.value);
    saleItem.value = res.content;
    } else {
      const res = await getItems(
          `${
            import.meta.env.VITE_APP_URL
          }/v2/sale-items?filterBrands=${filterBrandR.value}&sortDirection=${directionR.value}&page=${
            pageNumber.value
          }&size=${pageSize.value}`
        );
    pageObj.value = res;
    console.log("pageObj.value:", pageObj.value);
    saleItem.value = res.content;
    }
  } catch (err) {
    console.log(err);
  }
};

watch([pageSize, pageNumber], () => {
  sessionStorage.setItem("pageSize", pageSize.value);
  sessionStorage.setItem("pageNumber", pageNumber.value);
  fetchData();
});
watch([filterBrandR, directionR], () => {
  sessionStorage.setItem("filterBrand", JSON.stringify(filterBrandR.value));
  sessionStorage.setItem("direction", directionR.value);
  console.log("reset");
  pageNumber.value = 0;
});
watch(pageSize, () => {
  if (!pageSizeWatchInitialized) {
    pageSizeWatchInitialized = true;
    return;
  }
  pageNumber.value = 0;
});

watchEffect(() => {
  const filterBrandSession = sessionStorage.getItem("filterBrand");
  const directionSession = sessionStorage.getItem("direction");
  const pageNumberSession = sessionStorage.getItem("pageNumber");
  const pageSizeSession = sessionStorage.getItem("pageSize");
});

const isSuccess = ref(
  Boolean(route.query.alertAdd || route.query.alertDelete) &&
    !route.query.alert404
);

const filterAndSortSaleItem = async (filterBrand, direction, field) => {
  filterBrandR.value = filterBrand;
  directionR.value =direction;
  fieldR.value = field;
  try {
    if (!filterBrand || filterBrand.length === 0) {
      if (!direction && !field) {
        console.log("no filter createTime");
        const res = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?page=${pageNumber.value}&size=${pageSize.value}`);
        saleItem.value = res.content;
        pageObj.value = res;
      } else if(direction.toLowerCase() === "asc" || !direction) {
        console.log("no filter ASC");
        const res = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?page=${pageNumber.value}&size=${pageSize.value}&sortField=brand.name&sortDirection=asc`);
        saleItem.value = res.content;
        pageObj.value = res;
      } else {
        console.log("no filter DESC");
        const res = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?page=${pageNumber.value}&size=${pageSize.value}&sortField=brand.name&sortDirection=${direction}`);
        saleItem.value = res.content;
        pageObj.value = res;
      }
    } else {
        if (!direction && !field) {
          console.log("filter createTime");
          const res = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?filterBrands=${filterBrand}&page=${pageNumber.value}&size=${pageSize.value}`);
          saleItem.value = res.content;
          pageObj.value = res;
        } else if (direction.toLowerCase() === "asc" || !direction) {
          console.log("filter asc");
          const res = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?filterBrands=${filterBrand}&page=${pageNumber.value}&size=${pageSize.value}&sortField=brand.name&sortDirection=asc`);
          saleItem.value = res.content;
          pageObj.value = res;
        } else {
          console.log("filter DESC");
          const res = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?filterBrands=${filterBrand}&page=${pageNumber.value}&size=${pageSize.value}&sortField=brand.name&sortDirection=${direction}`);
          saleItem.value = res.content;
          pageObj.value = res;
        }
    }
  } catch(err) {
    console.log(err);
  }
} 
</script>
<template>
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
  <div class="pl-10 pr-10 pt-10 m-0 flex justify-between">
    <router-link :to="{ name: 'SaleItemAdd' }">
      <button
      @click="savePreviousPath"
        class="itbms-sale-item-add px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
      >
        Add Sale Item
      </button>
    </router-link>
    <div>
      <span>Show</span>
      <span class="ml-2">
        <select
          class="bg-gray-500 p-2 border itbms-page-size"
          v-model.number="pageSize"
        >
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
        </select></span
      >
    </div>
  </div>

  <FilterSaleItem 
    :brands="filterBrandR"
    :sortDirection="directionR"
    @filterAndSortSaleItem="filterAndSortSaleItem"
  ></FilterSaleItem>
  <SaleItemGallery :saleItems="saleItem"></SaleItemGallery>

  <div class="p-10 pt-0" v-show="pageObj.totalPages > 1">
    <button
      class="itbms-page-first cursor-pointer bg-gray-600 pl-5 pr-5 pt-3 pb-3"
      @click="pageNumber = 0"
      :class="isFirst ? 'opacity-45' : 'bg-gray-600'"
      :disabled="isFirst"
    >
      First
    </button>
    <button
      class="itbms-page-prev cursor-pointer bg-gray-600 pl-5 pr-5 pt-3 pb-3"
      @click="pageNumber = pageNumber - 1"
      :class="isFirst ? 'opacity-45' : 'bg-gray-600'"
      :disabled="isFirst"
    >
      Prev
    </button>
    <span
      v-for="index in computedPageNumberArr"
      :key="index"
      @click="pageNumber = index - 1"
      :class="`itbms-page-${index - 1} `"
    >
      <button
        class="pl-5 pr-5 pt-3 pb-3 cursor-pointer"
        :class="
          index - 1 === pageNumber ? 'bg-green-600 text-white' : 'bg-gray-600'
        "
      >
        {{ index }}
      </button>
    </span>
    <button
      class="itbms-page-next bg-gray-600 pl-5 pr-5 pt-3 pb-3 cursor-pointer"
      @click="pageNumber = pageNumber + 1"
      :class="isLast ? 'opacity-45' : 'bg-gray-600'"
      :disabled="isLast"
    >
      Next
    </button>
    <button
      class="itbms-page-last bg-gray-600 pl-5 pr-5 pt-3 pb-3 cursor-pointer"
      @click="pageNumber = pageObj.totalPages - 1"
      :class="isLast ? 'opacity-45' : 'bg-gray-600'"
      :disabled="isLast"
    >
      Last
    </button>
  </div>
</template>
<style scoped></style>
