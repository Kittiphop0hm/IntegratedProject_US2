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

// Updated filter states
const filterBrandSession = sessionStorage.getItem("filterBrand");
const directionSession = sessionStorage.getItem("direction");
const filterPriceSession = sessionStorage.getItem("filterPrice");
const filterStorageSizeSession = sessionStorage.getItem("filterStorageSize");

const filterBrandR = ref(filterBrandSession ? JSON.parse(filterBrandSession) : []);
const directionR = ref(directionSession ? directionSession : '');
const filterPriceR = ref(filterPriceSession ? JSON.parse(filterPriceSession) : null);
const filterStorageSizeR = ref(filterStorageSizeSession ? JSON.parse(filterStorageSizeSession) : []);
const fieldR = ref('');

const pageNumberSession = sessionStorage.getItem("pageNumber");
const pageSizeSession = sessionStorage.getItem("pageSize");

onMounted(() => {
  pageSize.value = pageSizeSession ? Number(pageSizeSession) : 10;
  pageNumber.value = pageNumberSession ? Number(pageNumberSession) : 0;
});

function savePreviousPath() {
  const previousPath = route.fullPath;
  sessionStorage.setItem("previousPath", previousPath);
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
  return arr;
});

// Build query parameters helper function
const buildQueryParams = () => {
  const params = new URLSearchParams();
  
  // Add pagination
  params.append('page', pageNumber.value);
  params.append('size', pageSize.value);
  
  // Add brand filter
  if (filterBrandR.value && filterBrandR.value.length > 0) {
    params.append('filterBrands', filterBrandR.value.join(','));
  }
  
  // Add price filter
  if (filterPriceR.value) {
    if (filterPriceR.value.min !== null && filterPriceR.value.min !== undefined) {
      params.append('minPrice', filterPriceR.value.min);
    }
    if (filterPriceR.value.max !== null && filterPriceR.value.max !== undefined) {
      params.append('maxPrice', filterPriceR.value.max);
    }
  }
  
  // Add storage size filter
  if (filterStorageSizeR.value && filterStorageSizeR.value.length > 0) {
    params.append('filterStorageSizes', filterStorageSizeR.value.join(','));
  }
  
  // Add sorting
  if (directionR.value && fieldR.value) {
    params.append('sortField', fieldR.value);
    params.append('sortDirection', directionR.value);
  } else if (directionR.value === 'asc' || directionR.value === 'desc') {
    params.append('sortField', 'brand.name');
    params.append('sortDirection', directionR.value);
  }
  
  return params.toString();
};

const fetchData = async () => {
  try {
    const queryString = buildQueryParams();
    const res = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?${queryString}`);
    
    pageObj.value = res;
    console.log("pageObj.value:", pageObj.value);
    saleItem.value = res.content;
  } catch (err) {
    console.log(err);
  }
};

watch([pageSize, pageNumber], () => {
  sessionStorage.setItem("pageSize", pageSize.value);
  sessionStorage.setItem("pageNumber", pageNumber.value);
  fetchData();
});

watch([filterBrandR, directionR, filterPriceR, filterStorageSizeR], () => {
  sessionStorage.setItem("filterBrand", JSON.stringify(filterBrandR.value));
  sessionStorage.setItem("direction", directionR.value);
  sessionStorage.setItem("filterPrice", JSON.stringify(filterPriceR.value));
  sessionStorage.setItem("filterStorageSize", JSON.stringify(filterStorageSizeR.value));
  console.log("Filters changed, resetting to page 0");
  pageNumber.value = 0;
}, { deep: true });

watch(pageSize, () => {
  if (!pageSizeWatchInitialized) {
    pageSizeWatchInitialized = true;
    return;
  }
  pageNumber.value = 0;
});

const isSuccess = ref(
  Boolean(route.query.alertAdd || route.query.alertDelete) &&
    !route.query.alert404
);

const filterAndSortSaleItem = async (filterBrand, direction, field, filters = {}) => {
  console.log("filterAndSortSaleItem called with:", { filterBrand, direction, field, filters });
  
  // Update all filter states
  filterBrandR.value = filterBrand || [];
  directionR.value = direction || '';
  fieldR.value = field || '';
  
  // Update additional filters from the new filter component
  if (filters.brands !== undefined) {
    filterBrandR.value = filters.brands || [];
  }
  if (filters.priceRange !== undefined) {
    filterPriceR.value = filters.priceRange;
  }
  if (filters.storageSizes !== undefined) {
    filterStorageSizeR.value = filters.storageSizes || [];
  }
  
  try {
    await fetchData();
  } catch(err) {
    console.log(err);
  }
};

const fecthItemFromPage = async(index) => {
  pageNumber.value = index - 1;
  await fetchData();
};
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
      @click="fecthItemFromPage(index)"
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