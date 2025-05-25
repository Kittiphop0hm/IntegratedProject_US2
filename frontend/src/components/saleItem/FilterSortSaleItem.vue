<script setup>
import { onMounted, ref } from "vue";
import { getItems } from "@/libs/fetchUtil";

const emit = defineEmits([
  "filterSaleItemByBrand",
  "sortSaleItemByBrand",
  "filterAndSortSaleItem",
]);
const filterBrand = ref([]);
const isDropFilterBrand = ref(false);
const brands = ref([]);
const sortDirection = ref("");

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
  emit("filterAndSortSaleItem", filterBrand.value, sortDirection.value);
};

const setFilterSortSaleItems = (brands, direction) => {
  sortDirection.value = direction;
  console.log(sortDirection.value);
  console.log(brands);
  emit("filterAndSortSaleItem", brands, direction);
};
</script>
<template>
  <div class="relative flex flex-col space-y-2 px-10 pt-5">
    <!-- Filter and Selected Brands -->
    <div class="flex flex-col w-[80%] space-y-4">
      <!-- Selected Brands -->
      <div class="w-[80%] bg-white p-4 border border-gray-300 rounded-lg shadow-md">
        <div class="flex flex-wrap gap-2">
          <div
            v-for="(brand, index) in filterBrand"
            :key="index"
            class="flex items-center bg-gray-100 border border-gray-300 rounded-full px-3 py-1 text-sm text-gray-700"
          >
            <span>{{ brand }}</span>
            <button
              @click="deleteBrand(index)"
              class="ml-2 text-gray-400 hover:text-red-500 focus:outline-none"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-4 w-4"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                stroke-width="2"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- Filter Actions -->
      <div class="flex items-center">
        <button
          @click="isDropFilterBrand = !isDropFilterBrand"
          class="flex items-center gap-2 bg-blue-500 text-white rounded-lg px-4 py-2 hover:bg-blue-600 focus:outline-none shadow-md"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            fill="currentColor"
            viewBox="0 0 24 24"
          >
            <path d="M12 15l-5-5h10z"></path>
          </svg>
          <span>Filter</span>
        </button>
        <button
          @click="clearFilterBrand"
          class="bg-red-500 text-white rounded-lg px-4 py-2 hover:bg-red-600 focus:outline-none shadow-md"
        >
          Clear
        </button>
      </div>
    </div>

    <!-- Dropdown Filter -->
    <div
      v-show="isDropFilterBrand"
      class="bg-white p-4 border border-gray-300 rounded-lg shadow-md"
    >
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        <div
          v-for="brand in brands"
          :key="brand.id"
          class="flex items-center gap-2 p-2 bg-gray-50 rounded-lg border border-gray-200 shadow-sm"
        >
          <input
            type="checkbox"
            :value="brand.name"
            v-model="filterBrand"
            class="h-5 w-5 text-blue-500 border-gray-300 rounded focus:ring-2 focus:ring-blue-300"
          />
          <label class="text-gray-700 text-sm font-medium">{{
            brand.name
          }}</label>
        </div>
      </div>
    </div>

    <!-- Sorting Section -->
    <div
      class="absolute flex top-15 right-0 transform -translate-y-1/2 bg-white border border-gray-300 rounded-lg shadow-lg p-2 space-y-2"
    >
      <button
        @click="setFilterSortSaleItems(filterBrand, 'default')"
        class="w-full flex items-center justify-center px-3 py-2 rounded hover:bg-blue-100 focus:outline-none"
        :class="sortDirection === 'default' ? 'bg-blue-500 text-white' : 'bg-gray-100 text-gray-800'"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-5 w-5"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          stroke-width="2"
        >
          <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16" />
        </svg>
      </button>
      <button
        @click="setFilterSortSaleItems(filterBrand, 'asc')"
        class="w-full flex items-center justify-center px-3 py-2 rounded hover:bg-blue-100 focus:outline-none"
        :class="sortDirection === 'asc' ? 'bg-blue-500 text-white' : 'bg-gray-100 text-gray-800'"
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
        @click="setFilterSortSaleItems(filterBrand, 'desc')"
        class="w-full flex items-center justify-center px-3 py-2 rounded hover:bg-blue-100 focus:outline-none"
        :class="sortDirection === 'desc' ? 'bg-blue-500 text-white' : 'bg-gray-100 text-gray-800'"
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
</template>
