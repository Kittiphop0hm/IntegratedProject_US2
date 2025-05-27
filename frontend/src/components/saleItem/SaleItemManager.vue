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
const sortDirection = ref("default");
// const valueSelected = ref(1);
let pageSizeWatchInitialized = false;
const pageSize = ref();
const pageNumber = ref();

// const pageSize = computed(() => {
//   return 10 / valueSelected.value;
// });
const filterBrandSession = sessionStorage.getItem("filterBrand");
const directionSession = sessionStorage.getItem("direction");
const filterBrandR = ref(filterBrandSession ? JSON.parse(filterBrandSession) : []);
const directionR = ref(directionSession ? directionSession : '');
onMounted(() => {
  const pageNumberSession = sessionStorage.getItem("pageNumber");
  const pageSizeSession = sessionStorage.getItem("pageSize");
  // const filterBrandSession = sessionStorage.getItem("filterBrand");
  // sessionStorage.getItem("direction");
  pageSize.value = pageSizeSession ? Number(pageSizeSession) : 10;
  pageNumber.value = pageNumberSession ? Number(pageNumberSession) : 0;
  // filterBrandR.value = filterBrandSession ? JSON.parse(filterBrandSession) : [];
  // directionR.value = directionSession ? directionSession : '';
  // console.log(pageSize.value);
  // console.log(pageNumber.value);
  // console.log("-------------- OnMounted OnMounted OnMounted -------------- ");
  // console.log("filterBrandR.value: " + filterBrandR.value);
  // console.log("directionR.value: " + directionR.value);
  // console.log("type of filterBrandR.value: " + typeof filterBrandR.value);
  // fetchData();
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
      console.log("difference is less than 10");
      console.log("difference:", difference);
      console.log(Math.abs(difference - 10));
      while (arr.length !== maxDisplay) {
        arr.shift();
      }
    }
  }
  return arr;
});

const fetchData = async () => {
  try {
    const res = await getItems(
          `${
            import.meta.env.VITE_APP_URL
          }/v2/sale-items?filterBrands=${filterBrandR.value}&sortDirection=${directionR.value}&page=${
            pageNumber.value
          }&size=${pageSize.value}`
        );
    pageObj.value = res;
    // console.log("pageObj.value:", pageObj.value);
    saleItem.value = res.content;
  } catch (err) {
    console.log(err);
  }
};

watch([pageSize, pageNumber], () => {
  sessionStorage.setItem("pageSize", pageSize.value);
  sessionStorage.setItem("pageNumber", pageNumber.value);
  // console.log(pageSize.value);
  // console.log(pageNumber.value);
  fetchData();
});
watch([filterBrandR, directionR], () => {
  sessionStorage.setItem("filterBrand", JSON.stringify(filterBrandR.value));
  sessionStorage.setItem("direction", directionR.value);
  console.log("reset");
  pageNumber.value = 0;
  // // sessionStorage.setItem("pageSize")
  // sessionStorage.setItem("pageNumber", 0);
});
watch(pageSize, () => {
  if (!pageSizeWatchInitialized) {
    pageSizeWatchInitialized = true;
    return;
  }
  console.log("watch pageSize");
  // if (newVal !== oldVal) {
  //   console.log("newVal: " + newVal)
  //   console.log("oldVal: " + oldVal)
  pageNumber.value = 0;
  // }
});

// onMounted(async () => {
//   try {
//     // saleItem.value = await getItems(
//     //   `${import.meta.env.VITE_APP_URL}/v1/sale-items`
//     // );
//     const res = await getItems(
//       `${
//         import.meta.env.VITE_APP_URL
//       }/v2/sale-items?page=0&size=${pageSize.value}`
//     );

//     saleItem.value = res.content;
//   } catch (err) {
//     console.log(err);
//   }
// });

watchEffect(() => {
  // console.log("pageNumber.value:", pageNumber.value);
  // console.log("computedPageNumberArr:", computedPageNumberArr.value);
  const filterBrandSession = sessionStorage.getItem("filterBrand");
  const directionSession = sessionStorage.getItem("direction");
  const pageNumberSession = sessionStorage.getItem("pageNumber");
  const pageSizeSession = sessionStorage.getItem("pageSize");
  // console.log("pageNumberSession: " + pageNumberSession);
  // console.log("pageSizeSession: " + pageSizeSession);
  // console.log("filterBrandSession: " + filterBrandSession);
  // console.log("directionSession: " + directionSession);
});

const isSuccess = ref(
  Boolean(route.query.alertAdd || route.query.alertDelete) &&
    !route.query.alert404
);



const filterAndSortSaleItem = async (filterBrand, direction, field) => {
  // let newFilterBrand = filterBrandSession ? JSON.parse(filterBrandSession): filterBr 
  // let newDirection = directionSession ? directionSession : direction;
  // filterBrandR.value = filterBrand;
  // directionR.value =direction;
  console.log("---------------- filterAndSortSaleItem In Manager.vue ----------------")   
  // console.log("filterBrand: " + filterBrand);
  // console.log("direction: " + direction);
  // console.log(!direction ? "empty" : "not empty");
  // console.log(pageSize.value);
  // console.log(field);
  console.log(direction);
  console.log(field);
  
  
  try {
    if (filterBrand.length > 0) {
      if (!direction && !field) {
        console.log("createTime");
        const res = await getItems(
          `${import.meta.env.VITE_APP_URL}/v2/sale-items?filterBrands=${filterBrand}&page=${pageNumber.value}&size=${pageSize.value}`
        );
        saleItem.value = res.content; 
      } else if (!direction && field) {
        console.log('filter by field');
        const res = await getItems(
          `${import.meta.env.VITE_APP_URL}/v2/sale-items?filterBrands=${filterBrand}&page=${pageNumber.value}&size=${pageSize.value}&sortField=brand.name`
        );
        saleItem.value = res.content; 
      } else if (direction === "default") {
        const res = await getItems(
          `${
            import.meta.env.VITE_APP_URL
          }/v2/sale-items?filterBrands=${filterBrand}&page=${
            pageNumber.value
          }&size=${pageSize.value}`
        );
        saleItem.value = res.content;

      } else {
        const res = await getItems(
          `${
            import.meta.env.VITE_APP_URL
          }/v2/sale-items?filterBrands=${filterBrand}&sortField=brand.name&sortDirection=${direction}&page=${
            pageNumber.value
          }&size=${pageSize.value}`
        );
        saleItem.value = res.content;

      }
    } else {
      console.log("filterBrand is empty");
      if (direction === "default") {
        console.log("no filter Default");
        const res = await getItems(
          `${import.meta.env.VITE_APP_URL}/v2/sale-items?page=${
            pageNumber.value
          }&size=${pageSize.value}`
        );
        saleItem.value = res.content;

      } else {
        console.log("no filter ASC DESC");
        console.log(direction);
        console.log(directionR.value);
        
        const res = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?page=${pageNumber.value}&size=${pageSize.value}&sortDirection=${direction}`);
        saleItem.value = res.content;

      }
    }
    console.log(saleItem.value);
    
  } catch (err) {
    console.log(err);
  }
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
    <!-- <SortSaleItemByBrandname @sortSaleItemByBrand="filterAndSortSaleItem" /> -->
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
