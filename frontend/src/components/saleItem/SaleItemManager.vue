<script setup>
import SaleItemGallery from "../saleItem/SaleItemGallery.vue";
import { ref, onMounted, computed, watchEffect, watch } from "vue";
import { getItems, getItemsWithToken } from "../../libs/fetchUtil.js";
import { decodeJWT } from "@/libs/decodeJWT.js";
import { useRouter, useRoute } from "vue-router";
import AlertMessageModel from "../model/AlertMessageModel.vue";
import FilterSaleItem from "./FilterSortSaleItem.vue";
import SearchComponent from "../Search.vue";
const accessToken = sessionStorage.getItem("accessToken");
const getUser = decodeJWT(accessToken);
const route = useRoute();
const saleItem = ref([]);
const pageObj = ref({});
import { useUserStore } from "@/stores/users";
const userStore = useUserStore();
import { useCartStore } from "@/stores/carts.js";
import { useCountNewOrder } from "@/stores/countNewOrder";
const cartStore = useCartStore();
const router = useRouter();
let pageSizeWatchInitialized = false;
const pageSize = ref();
const pageNumber = ref();
const {fetchCountNewOrder} = useCountNewOrder()

// Updated filter states - เพิ่ม searchKeyword
const filterBrandSession = sessionStorage.getItem("filterBrand");
const directionSession = sessionStorage.getItem("direction");
const filterPriceSession = sessionStorage.getItem("filterPrice");
const filterStorageSizeSession = sessionStorage.getItem("filterStorageSize");
const searchKeywordSession = sessionStorage.getItem("searchKeyword");

const filterBrandR = ref(
  filterBrandSession ? JSON.parse(filterBrandSession) : []
);
const directionR = ref(directionSession ? directionSession : "");
const filterPriceR = ref(
  filterPriceSession ? JSON.parse(filterPriceSession) : null
);
const filterStorageSizeR = ref(
  filterStorageSizeSession ? JSON.parse(filterStorageSizeSession) : []
);
const searchKeywordR = ref(searchKeywordSession ? searchKeywordSession : ""); // เพิ่ม search keyword
const fieldR = ref("");

const pageNumberSession = sessionStorage.getItem("pageNumber");
const pageSizeSession = sessionStorage.getItem("pageSize");

onMounted(async () => {
  pageSize.value = pageSizeSession ? Number(pageSizeSession) : 10;
  pageNumber.value = pageNumberSession ? Number(pageNumberSession) : 0;
  await fetchCountNewOrder()
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

const buildQueryParams = () => {
  const params = new URLSearchParams();

  // Add pagination
  params.append("page", pageNumber.value);
  params.append("size", pageSize.value);

  // Add search keyword (เพิ่มตรงนี้)
  if (searchKeywordR.value && searchKeywordR.value.trim()) {
    params.append("searchKeyWord", searchKeywordR.value.trim());
  }

  // Add brand filter
  if (filterBrandR.value && filterBrandR.value.length > 0) {
    params.append("filterBrands", filterBrandR.value.join(","));
  }

  // Add price filter
  if (filterPriceR.value) {
    if (
      filterPriceR.value.min !== null &&
      filterPriceR.value.min !== undefined
    ) {
      params.append("filterPriceLower", filterPriceR.value.min);
    }
    if (
      filterPriceR.value.max !== null &&
      filterPriceR.value.max !== undefined
    ) {
      params.append("filterPriceUpper", filterPriceR.value.max);
    }
  }

  // Add storage size filter
  if (filterStorageSizeR.value && filterStorageSizeR.value.length > 0) {
    params.append("filterStorages", filterStorageSizeR.value.join(","));
  }

  // Add sorting
  if (directionR.value && fieldR.value) {
    params.append("sortField", fieldR.value);
    params.append("sortDirection", directionR.value);
  } else if (directionR.value === "asc" || directionR.value === "desc") {
    params.append("sortField", "brand.name");
    params.append("sortDirection", directionR.value);
  }

  return params.toString();
};

const fetchData = async () => {
  try {
    console.log(accessToken)
    const queryString = buildQueryParams();
    let res = null;
    if (!accessToken) {
      console.log("no token")
       res = await getItems(
        `${import.meta.env.VITE_APP_URL}/v2/sale-items?${queryString}`
      );
    } else {
       res = await getItemsWithToken(
        `${import.meta.env.VITE_APP_URL}/v2/sale-items?${queryString}`,
        accessToken
      );
    }
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

// เพิ่ม searchKeywordR ใน watch
watch(
  [filterBrandR, directionR, filterPriceR, filterStorageSizeR, searchKeywordR],
  () => {
    sessionStorage.setItem("filterBrand", JSON.stringify(filterBrandR.value));
    sessionStorage.setItem("direction", directionR.value);
    sessionStorage.setItem("filterPrice", JSON.stringify(filterPriceR.value));
    sessionStorage.setItem(
      "filterStorageSize",
      JSON.stringify(filterStorageSizeR.value)
    );
    sessionStorage.setItem("searchKeyword", searchKeywordR.value);
    console.log("Filters changed, resetting to page 0");
    pageNumber.value = 0;
  },
  { deep: true }
);

watch(pageSize, () => {
  if (!pageSizeWatchInitialized) {
    pageSizeWatchInitialized = true;
    return;
  }
  pageNumber.value = 0;
});

const isSuccess = ref(
  Boolean(
    route.query.alertAdd || route.query.alertDelete || route.query.alertAddUser
  ) && !route.query.alert404
);

const filterAndSortSaleItem = async (
  filterBrand,
  direction,
  field,
  filters = {}
) => {
  console.log("filterAndSortSaleItem called with:", {
    filterBrand,
    direction,
    field,
    filters,
  });

  filterBrandR.value = filterBrand || [];
  directionR.value = direction || "";
  fieldR.value = field || "";

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
  } catch (err) {
    console.log(err);
  }
};

const fecthItemFromPage = async (index) => {
  pageNumber.value = index - 1;
  await fetchData();
};

const handleSearch = (keyword) => {
  console.log("Search triggered with keyword:", keyword);
  searchKeywordR.value = keyword;
  pageNumber.value = 0;
  fetchData();
};

const isShowAlertMessageModel = ref(false);
const messageAlert = ref("");
const checkRole = (yourItem) => {
  console.log("checkRole called with item:", yourItem);
  if(userStore.role === "") {
    console.log("no role stupid 250 SaleitemMnaager")
    router.push({ name: "Login" });
    return
  }

  if (yourItem.seller.id === userStore.id) {
    isShowAlertMessageModel.value = true;
    isSuccess.value = false;
    messageAlert.value = "You cannot add your own item to the cart.";
  
  } else {
    console.log("test Manager")
    console.log("yourItem.quantity:", yourItem.quantity);
    yourItem.quantityEach = 1
    const result = cartStore.isMaxQtyInStock( yourItem );
    console.log("Result from isMaxQtyInStock:", result);
    if(typeof result === 'string') {
      isShowAlertMessageModel.value = true;
      isSuccess.value = false;
      messageAlert.value = result;
    }
    else {
      console.log("Error mai")
      cartStore.pushInCart(yourItem);
      alert("Ok Herbal")
    }
  }
};

</script>

<template>
  <SearchComponent @search="handleSearch" />

  <div
    v-show="
      route.query.alertAdd ||
      route.query.alertDelete ||
      route.query.alert404 ||
      route.query.alertAddUser ||
      isShowAlertMessageModel === true
    "
    class="p-10 pb-0"
  >
    <AlertMessageModel :isSuccess="isSuccess">
      <template #message>
        <p class="itbms-message" v-show="isSuccess === true">
          {{
            route.query.alertAddUser
              ? "The user account has been"
              : "The sale item has been"
          }}
          <span class="text-green-400">
            {{
              route.query.alertAdd
                ? "successfully added."
                : "deleted." || route.query.alertAddUser
                ? "successfully registered."
                : "deleted."
            }}
          </span>
        </p>
        <p class="itbms-message" v-show="isSuccess === false">
          <p v-if="isShowAlertMessageModel === true && isSuccess === false">{{ messageAlert }}</p>
          <p v-else>The requested sale item does not exist.</p>
        </p>
      </template>
    </AlertMessageModel>
  </div>
  <div class="pl-10 pr-10 pt-10 m-0 flex justify-between">
    <router-link :to="{ name: 'SaleItemAdd' }">
      <button
        @click="savePreviousPath"
        class="flex justify-center items-center gap-x-2 itbms-sale-item-add px-4 py-4 bg-[#523F31] text-white font-semibold cursor-pointer rounded-md hover:opacity-90"
      >
        <p>Add SaleItem</p>
        <svg xmlns="http://www.w3.org/2000/svg" width="23" height="23" viewBox="0 0 24 24"><g fill="none"><circle cx="12" cy="12" r="9" fill="#ffffff"/><path stroke="#000000" stroke-linecap="square" stroke-linejoin="round" stroke-width="1.2" d="M12 8v8m4-4H8"/></g></svg>
      </button>
    </router-link>
    <div>
      <span class="ml-2">
        <select
          defaultValue="Pick a color" className="select bg-[#523F31] text-white font-semibold"
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

  <SaleItemGallery 
  :saleItems="saleItem"
  @addToCart="checkRole"
  ></SaleItemGallery>

  <div class="p-10 pt-0 text-white" v-show="pageObj.totalPages > 1">
    <button
      class="itbms-page-first cursor-pointer bg-[#796254] inset-shadow-xs shadow-black shadow-[0_4px_6px_rgba(82,63,90,1)] pl-5 pr-5 pt-3 pb-3 rounded-l-lg"
      @click="pageNumber = 0"
      :class="isFirst ? 'opacity-60' : 'bg-[#796254]'"
      :disabled="isFirst"
    >
      First
    </button>
    <button
      class="itbms-page-prev cursor-pointer bg-[#796254] inset-shadow-xs shadow-black shadow-[0_4px_6px_rgba(82,63,90,1)] pl-5 pr-5 pt-3 pb-3"
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
        class="pl-5 pr-5 pt-3 pb-3 inset-shadow-xs shadow-black font-semibold shadow-[0_4px_6px_rgba(82,63,90,1)] cursor-pointer"
        :class="
          index - 1 === pageNumber ? 'bg-[#523F31] text-white' : 'bg-[#796254]'
        "
      >
        {{ index }}
      </button>
    </span>
    <button
      class="itbms-page-next bg-[#796254] inset-shadow-xs shadow-black shadow-[0_4px_6px_rgba(82,63,90,1)] pl-5 pr-5 pt-3 pb-3 cursor-pointer"
      @click="pageNumber = pageNumber + 1"
      :class="isLast ? 'opacity-60' : 'bg-[#523F31]'"
      :disabled="isLast"
    >
      Next
    </button>
    <button
      class="itbms-page-last rounded-r-lg bg-[#796254] inset-shadow-xs shadow-black shadow-[0_4px_6px_rgba(82,63,90,1)] pl-5 pr-5 pt-3 pb-3 cursor-pointer"
      @click="pageNumber = pageObj.totalPages - 1"
      :class="isLast ? 'opacity-60' : 'bg-[#523F31]'"
      :disabled="isLast"
    >
      Last
    </button>
  </div>
</template>

<style scoped></style>
