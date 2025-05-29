<script setup>
import { onMounted, ref, watch } from "vue";
import { getItems } from "@/libs/fetchUtil";

const props = defineProps({
  brands: Array,
  sortDirection: String,
});
const filterBrand = ref(props.brands);
const sortDirection = ref(props.sortDirection);

const emit = defineEmits([
  "filterSaleItemByBrand",
  "sortSaleItemByBrand",
  "filterAndSortSaleItem",
]);

const isDropFilterBrand = ref(false);
const brands = ref([]);
onMounted(async () => {
  try {
    brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`);
    brands.value.sort((a, b) => a.name.localeCompare(b.name));
  } catch (err) {
    console.log(err);
  }
});

const clearFilterBrand = () => {
  filterBrand.value = [];
  isDropFilterBrand.value = false;
  emit("filterAndSortSaleItem", filterBrand.value, sortDirection.value);
};

const deleteBrand = (index) => {
  filterBrand.value.splice(index, 1);
  sessionStorage.setItem("filterBrand", JSON.stringify(filterBrand.value));
  emit("filterAndSortSaleItem", filterBrand.value, sortDirection.value);
};

const setFilterSortSaleItems = (brands, direction, field) => {
  const directionSession = sessionStorage.getItem("direction");
  sortDirection.value = directionSession ? directionSession : direction;
  emit("filterAndSortSaleItem", brands, direction, field);
};
</script>

<template>
  <div class="relative top-10 flex items-center justify-between px-10 mb-5">
    <div class="itbms-brand-filter w-[70%] h-full rounded-lg mr-2">
      <div
        class="w-full flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0"
      >
        <div
          class="flex flex-wrap items-center gap-2 w-full sm:w-[75%] p-2 py-5 border border-gray-300 bg-white rounded-l-lg"
        >
          <div
            class="flex items-center"
          >
            <span
              v-for="(brand, index) in filterBrand"
              :key="index"
              class="itbms-filter-item bg-gray-100 border border-gray-300 rounded-full px-3 py-1 text-sm text-gray-700"
              >{{ brand }}
              <button
              @click="deleteBrand(index)"
              class="itbms-filter-item-clear ml-2 text-gray-400 hover:text-red-500 focus:outline-none"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                fill="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  d="M19 7L17.59 5.59L12 11.17L6.41 5.59L5 7l5.59 5.59L5 18.17l1.41 1.42L12 13.83l5.59 5.59L19 18.17l-5.59-5.59z"
                ></path>
              </svg>
            </button>  
            </span
            >

          </div>
        </div>

        <div class="flex">
          <button
            @click="isDropFilterBrand = !isDropFilterBrand"
            class="itbms-brand-filter-button flex items-center justify-center bg-blue-500 border border-gray-300 text-white px-4 py-2 hover:bg-blue-600 focus:outline-none"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="20"
              height="20"
              viewBox="0 0 24 24"
              class="inline"
            >
              <path fill="currentColor" d="m12 15l-5-5h10z"></path>
            </svg>
            <span>Filter</span>
          </button>
          <button
            @click="clearFilterBrand"
            class="itbms-brand-filter-clear bg-red-500 text-white px-4 py-2 border border-gray-300 hover:bg-red-600 focus:outline-none rounded-r-lg"
          >
            Clear
          </button>
        </div>
      </div>

      <div
        v-if="isDropFilterBrand"
        class="w-[50.3%] bg-white border border-gray-300 p-5 shadow-md"
      >
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          <div
            v-for="brand in brands"
            :key="brand.id"
            class="flex items-center space-x-3 p-2 bg-gray-50 rounded-lg border border-gray-200 shadow-sm"
          >
            <input
              @change="setFilterSortSaleItems(filterBrand, sortDirection, '')"
              :id="brand.name"
              type="checkbox"
              :value="brand.name"
              v-model="filterBrand"
              class="itbms-brand h-5 w-5 text-blue-500 border-gray-300 rounded focus:ring-2 focus:ring-blue-300"
            />
            <label
              :for="brand.name"
              class="text-gray-700 text-sm font-medium itbms-filter-item"
              >{{ brand.name }}</label
            >
          </div>
        </div>
      </div>
    </div>

    <div class="absolute right-10 top-3 flex items-center space-x-2 justify-end">
      <!-- <span class="text-sm font-medium text-gray-700">Sort Brand : </span> -->
      <div
        class="itbms-brand-sort flex border border-gray-300 rounded-lg overflow-hidden"
      >
        <button
          @click="setFilterSortSaleItems(filterBrand, '', '')"
          class="itbms-brand-none px-3 py-2 hover:bg-blue-100 transition"
          :class="
            sortDirection === ''
              ? 'bg-blue-500 text-white'
              : 'bg-white text-gray-800'
          "
          aria-label="Sort Default"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            stroke-width="2"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M4 6h16M4 12h16M4 18h16"
            />
          </svg>
        </button>
        <button
          @click="setFilterSortSaleItems(filterBrand, 'asc', 'brand.name')"
          class="itbms-brand-asc px-3 py-2 hover:bg-blue-100 transition"
          :class="
            sortDirection === 'asc'
              ? 'bg-blue-500 text-white'
              : 'bg-white text-gray-800'
          "
          aria-label="Sort A to Z"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            stroke-width="2"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M16 17v-8m0 0l-3 3m3-3l3 3M4 4h6M4 8h4M4 12h2"
            />
          </svg>
        </button>

        <button
          @click="setFilterSortSaleItems(filterBrand, 'desc', 'brand.name')"
          class="itbms-brand-desc px-3 py-2 hover:bg-blue-100 transition"
          :class="
            sortDirection === 'desc'
              ? 'bg-blue-500 text-white'
              : 'bg-white text-gray-800'
          "
          aria-label="Sort Z to A"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            stroke-width="2"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M16 7v8m0 0l-3-3m3 3l3-3M4 4h6M4 8h4M4 12h2"
            />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>
