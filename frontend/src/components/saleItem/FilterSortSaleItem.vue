<script setup>
import { onMounted, ref, watch, watchEffect } from "vue";
import { getItems } from "@/libs/fetchUtil";

const props = defineProps({
  brands: Array,
  sortDirection: String,
});
const filterBrand = ref(props.brands);
const sortDirection = ref(props.sortDirection);
const filterPrice = ref(0); // เก็บช่วงราคาที่เลือก
const filterStorageSize = ref([]);

watchEffect(() => {
  console.log(filterStorageSize.value);
})// เปลี่ยนเป็น array เพื่อเก็บหลายค่า

const emit = defineEmits([
  "filterSaleItemByBrand",
  "sortSaleItemByBrand",
  "filterAndSortSaleItem",
]);

const isDropFilterBrand = ref(false);
const isDropFilterPrice = ref(false);
const isDropFilterStorageSize = ref(false);
const brands = ref([]);
const storageSizes = ref([]);

// ช่วงราคาที่กำหนด
const priceRanges = [
  { label: "0-5,000", min: 0, max: 5000 },
  { label: "5,001-10,000", min: 5001, max: 10000 },
  { label: "10,001-20,000", min: 10001, max: 20000 },
  { label: "20,001-30,000", min: 20001, max: 30000 },
  { label: "30,001-40,000", min: 30001, max: 40000 },
  { label: "40,001-50,000", min: 40001, max: 50000 },
];

// สำหรับ custom price range
const customPriceMin = ref('');
const customPriceMax = ref('');

// ฟังก์ชันเรียงลำดับ storage size
const sortStorageSizes = (sizes) => {
  return sizes.sort((a, b) => {
    // "Not specified" ไว้ท้ายสุด
    if (a.name === "Not specified") return 1;
    if (b.name === "Not specified") return -1;
    
    // แปลงค่าเป็นตัวเลขสำหรับการเรียงลำดับ
    const getValue = (size) => {
      const name = size.name.toLowerCase();
      if (name.includes('tb')) {
        return parseFloat(name) * 1024; // แปลง TB เป็น GB
      } else if (name.includes('gb')) {
        return parseFloat(name);
      } else {
        return parseFloat(name) / 1024; // สมมติว่าเป็น MB แปลงเป็น GB
      }
    };
    
    return getValue(a) - getValue(b);
  });
};

const mockStorageSizes = ref([
    { id: 1, name: "32Gb", value: 32 },
    { id: 2, name: "64Gb", value: 64},
    { id: 3, name: "128Gb", value: 128},
    { id: 4, name: "256Gb", value: 256 },
    { id: 5, name: "512Gb", value: 512},
    { id: 6, name: "1Tb", value: 1024},
    { id: 7, name: "Not specified", value: 0} 
]);

// ฟังก์ชันแปลงค่าสำหรับส่งไป backend
const convertStorageSizesForBackend = (storageSizes) => {
  return storageSizes.map(size => {
    // ถ้าค่าเป็น 0 ให้ส่งเป็น "0"
    if (size === 0) {
      return "0";
    }
    return size.toString();
  });
};

// ฟังก์ชันหลักสำหรับ emit ข้อมูล
const emitFilterAndSort = () => {
  const convertedStorageSizes = convertStorageSizesForBackend(filterStorageSize.value);
  emit("filterAndSortSaleItem", filterBrand.value, sortDirection.value, "", {
    brands: filterBrand.value,
    priceRange: filterPrice.value,
    storageSizes: convertedStorageSizes
  });
};

onMounted(async () => {
  try {
    // ดึงข้อมูล brands
    brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`);
    brands.value.sort((a, b) => a.name.localeCompare(b.name));
  
    // โหลด filters จาก localStorage
    const savedBrandFilter = localStorage.getItem("filterBrand");
    const savedPriceFilter = localStorage.getItem("filterPrice");
    const savedStorageSizeFilter = localStorage.getItem("filterStorageSize");
    
    if (savedBrandFilter) {
      filterBrand.value = JSON.parse(savedBrandFilter);
    }
    if (savedPriceFilter) {
      filterPrice.value = JSON.parse(savedPriceFilter);
    }
    if (savedStorageSizeFilter) {
      filterStorageSize.value = JSON.parse(savedStorageSizeFilter);
    }
  } catch (err) {
    console.log(err);
  }
});

// ล้างทั้ง brand, price และ storage size filters
const clearAllFilters = () => {
  filterBrand.value = [];
  filterPrice.value = null;
  filterStorageSize.value = []; // ล้างเป็น array ว่าง
  customPriceMin.value = '';
  customPriceMax.value = '';
  isDropFilterBrand.value = false;
  isDropFilterPrice.value = false;
  isDropFilterStorageSize.value = false;
  localStorage.removeItem("filterBrand");
  localStorage.removeItem("filterPrice");
  localStorage.removeItem("filterStorageSize");
  emitFilterAndSort();
};

const deleteBrand = (index) => {
  filterBrand.value.splice(index, 1);
  localStorage.setItem("filterBrand", JSON.stringify(filterBrand.value));
  emitFilterAndSort();
};

// เลือกช่วงราคา
const selectPriceRange = (range) => {
  filterPrice.value = range;
  customPriceMin.value = '';
  customPriceMax.value = '';
  isDropFilterPrice.value = false;
  localStorage.setItem("filterPrice", JSON.stringify(range));
  emitFilterAndSort();
};

// ตั้งค่า custom price range
const setCustomPriceRange = () => {
  const minPrice = customPriceMin.value ? parseFloat(customPriceMin.value) : null;
  const maxPrice = customPriceMax.value ? parseFloat(customPriceMax.value) : null;
  
  if (minPrice !== null && maxPrice !== null && maxPrice < minPrice) {
    return; // ไม่ทำอะไรถ้าราคาสูงสุดน้อยกว่าต่ำสุด
  }
  
  if (minPrice !== null || maxPrice !== null) {
    const customRange = {
      label: minPrice === maxPrice ? `${minPrice}` : 
             maxPrice === null ? `${minPrice}` :
             minPrice === null ? `0-${maxPrice}` : 
             `${minPrice}-${maxPrice}`,
      min: minPrice,
      max: maxPrice
    };
    
    filterPrice.value = customRange;
    isDropFilterPrice.value = false;
    localStorage.setItem("filterPrice", JSON.stringify(customRange));
    emitFilterAndSort();
  }
};

// ลบช่วงราคาที่เลือก
const removePriceFilter = () => {
  filterPrice.value = null;
  localStorage.removeItem("filterPrice");
  emitFilterAndSort();
};

// ลบ storage size ที่เลือกเฉพาะรายการ
const removeStorageSizeFilter = (storageToRemove) => {
  const index = filterStorageSize.value.indexOf(storageToRemove);
  if (index > -1) {
    filterStorageSize.value.splice(index, 1);
    localStorage.setItem("filterStorageSize", JSON.stringify(filterStorageSize.value));
    emitFilterAndSort();
  }
};

// ล้าง storage size ทั้งหมด
const clearAllStorageSizeFilters = () => {
  filterStorageSize.value = [];
  localStorage.removeItem("filterStorageSize");
  emitFilterAndSort();
};

const setFilterSortSaleItems = (brands, direction, field) => {
  const directionSession = localStorage.getItem("direction");
  sortDirection.value = directionSession ? directionSession : direction;
  const convertedStorageSizes = convertStorageSizesForBackend(filterStorageSize.value);
  emit("filterAndSortSaleItem", brands, direction, field, {
    brands: brands,
    priceRange: filterPrice.value,
    storageSizes: convertedStorageSizes
  });
};

// ฟังก์ชันสำหรับการเปลี่ยนแปลง storage size filter
const onStorageSizeChange = () => {
  localStorage.setItem("filterStorageSize", JSON.stringify(filterStorageSize.value));
  emitFilterAndSort();
};
</script>

<template>
  <div class="relative top-10 px-10 mb-5">
    <!-- แถว Brand + Price + Storage Size + Clear + Sort -->
    <div class="flex items-start justify-between mb-2">
      <!-- ปุ่ม Brand + Price + Storage Size + Clear -->
      <div class="flex items-center space-x-4">
        <!-- ปุ่ม Brand -->
        <button
          @click="isDropFilterBrand = !isDropFilterBrand"
          class="itbms-brand-filter flex items-center justify-start bg-white border-2 border-gray-300 text-gray-700 px-5 py-3 hover:border-gray-400 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-gray-200 rounded-lg transition-all duration-200"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            class="inline transition-transform duration-200"
            :class="isDropFilterBrand ? 'rotate-180' : ''"
          >
            <path fill="currentColor" d="m12 15l-5-5h10z"></path>
          </svg>
          <span class="ml-2 font-medium">Brand</span>
          <span 
            v-if="filterBrand.length > 0" 
            class="ml-2 bg-gray-600 text-white text-xs font-bold px-2 py-1 rounded-full"
          >
            {{ filterBrand.length }}
          </span>
        </button>

        <!-- ปุ่ม Price -->
        <button
          @click="isDropFilterPrice = !isDropFilterPrice"
          class="itbms-price-filter flex items-center justify-start bg-white border-2 border-gray-300 text-gray-700 px-5 py-3 hover:border-gray-400 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-gray-200 rounded-lg transition-all duration-200"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            class="inline transition-transform duration-200"
            :class="isDropFilterPrice ? 'rotate-180' : ''"
          >
            <path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"></path>
          </svg>
          <span class="ml-2 font-medium">Price</span>
          <span 
            v-if="filterPrice" 
            class="ml-2 bg-gray-600 text-white text-xs font-bold px-2 py-1 rounded-full"
          >
            1
          </span>
        </button>

        <!-- ปุ่ม Storage Size -->
        <button
          @click="isDropFilterStorageSize = !isDropFilterStorageSize"
          class="itbms-storage-size-filter flex items-center justify-start bg-white border-2 border-gray-300 text-gray-700 px-5 py-3 hover:border-gray-400 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-gray-200 rounded-lg transition-all duration-200"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            class="inline transition-transform duration-200 "
            :class="isDropFilterStorageSize ? 'rotate-180' : ''"
          >
            <path fill="currentColor" d="M4 6h16v2H4zm0 5h16v2H4zm0 5h16v2H4z"></path>
          </svg>
          <span class="ml-2 font-medium">Storage Size</span>
          <span 
            v-if="filterStorageSize.length > 0" 
            class="ml-2 bg-gray-600 text-white text-xs font-bold px-2 py-1 rounded-full"
          >
            {{ filterStorageSize.length}}
          </span>
        </button>

        <!-- ปุ่ม Clear -->
        <button
          @click="clearAllFilters"
          class="itbms-brand-filter-clear bg-white border-2 border-gray-300 text-gray-700 px-5 py-3 hover:border-red-400 hover:text-red-600 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-red-200 rounded-lg transition-all duration-200 font-medium"
        >
          Clear
        </button>
      </div>

      <!-- Sort ด้านขวา -->
      <div class="flex items-center space-x-2">
        <span class="text-sm text-gray-600 font-medium mr-2">Sort:</span>
        <div class="itbms-brand-sort flex border-2 border-gray-300 rounded-lg overflow-hidden shadow-sm bg-white">
          <button
            @click="setFilterSortSaleItems(filterBrand, '', '')"
            class="itbms-brand-none px-4 py-3 hover:bg-gray-50 transition-all duration-200"
            :class="sortDirection === '' ? 'bg-gray-600 text-white' : 'bg-white text-gray-800'"
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
              <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>

          <button
            @click="setFilterSortSaleItems(filterBrand, 'asc', 'brand.name')"
            class="itbms-brand-asc px-4 py-3 hover:bg-gray-50 transition-all duration-200 border-l-2 border-gray-300"
            :class="sortDirection === 'asc' ? 'bg-gray-600 text-white' : 'bg-white text-gray-800'"
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
              <path stroke-linecap="round" stroke-linejoin="round" d="M16 17v-8m0 0l-3 3m3-3l3 3M4 4h6M4 8h4M4 12h2" />
            </svg>
          </button>

          <button
            @click="setFilterSortSaleItems(filterBrand, 'desc', 'brand.name')"
            class="itbms-brand-desc px-4 py-3 hover:bg-gray-50 transition-all duration-200 border-l-2 border-gray-300"
            :class="sortDirection === 'desc' ? 'bg-gray-600 text-white' : 'bg-white text-gray-800'"
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
              <path stroke-linecap="round" stroke-linejoin="round" d="M16 7v8m0 0l-3-3m3 3l3-3M4 4h6M4 8h4M4 12h2" />
            </svg>
          </button>
        </div>
      </div>
    </div>

    <!-- Filter tags -->
    <div class="flex flex-wrap items-center gap-3 p-4 bg-gray-50 rounded-lg mb-4 shadow-sm border border-gray-200">
      <span class="text-sm font-medium text-gray-600">Active Filters:</span>
      
      <!-- Brand Filter Tags -->
      <span
        v-for="(brand, index) in filterBrand"
        :key="'brand-' + index"
        class="itbms-brand-item bg-white border-2 border-gray-300 rounded-full px-4 py-2 text-sm text-gray-800 shadow-sm hover:shadow-md transition-all duration-200 flex items-center"
      >
        {{ brand }}
        <button
          @click="deleteBrand(index)"
          class="itbms-brand-item-clear ml-2 text-gray-500 hover:text-red-500 focus:outline-none hover:bg-gray-100 rounded-full p-1 transition-all duration-200"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="14"
            height="14"
            fill="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              d="M19 7L17.59 5.59L12 11.17L6.41 5.59L5 7l5.59 5.59L5 18.17l1.41 1.42L12 13.83l5.59 5.59L19 18.17l-5.59-5.59z"
            ></path>
          </svg>
        </button>
      </span>

      <!-- Price Filter Tag -->
      <span
        v-if="filterPrice"
        class="itbms-price-item bg-white border-2 border-gray-300 rounded-full px-4 py-2 text-sm text-gray-800 shadow-sm hover:shadow-md transition-all duration-200 flex items-center"
      >
        {{ filterPrice.min && filterPrice.max ? 
            `${filterPrice.min.toLocaleString()}-${filterPrice.max.toLocaleString()}` : 
            filterPrice.label }} Baht
        <button
          @click="removePriceFilter"
          class="itbms-price-item-clear ml-2 text-gray-500 hover:text-red-500 focus:outline-none hover:bg-gray-100 rounded-full p-1 transition-all duration-200"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="14"
            height="14"
            fill="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              d="M19 7L17.59 5.59L12 11.17L6.41 5.59L5 7l5.59 5.59L5 18.17l1.41 1.42L12 13.83l5.59 5.59L19 18.17l-5.59-5.59z"
            ></path>
          </svg>
        </button>
      </span>

      <!-- Storage Size Filter Tags - แสดงแต่ละรายการที่เลือก -->
      <span
        v-for="(storage, index) in filterStorageSize"
        :key="'storage-' + index"
        class="itbms-storage-size-item bg-white border-2 border-gray-300 rounded-full px-4 py-2 text-sm text-gray-800 shadow-sm hover:shadow-md transition-all duration-200 flex items-center"
      >
        {{ storage === 0 ? 'Not specified' : storage + 'Gb' }}
        <button
          @click="removeStorageSizeFilter(storage)"
          class="itbms-storage-size-item-clear ml-2 text-gray-500 hover:text-red-500 focus:outline-none hover:bg-gray-100 rounded-full p-1 transition-all duration-200"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="14"
            height="14"
            fill="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              d="M19 7L17.59 5.59L12 11.17L6.41 5.59L5 7l5.59 5.59L5 18.17l1.41 1.42L12 13.83l5.59 5.59L19 18.17l-5.59-5.59z"
            ></path>
          </svg>
        </button>
      </span>

      <!-- แสดงข้อความเมื่อไม่มี filter -->
      <span 
        v-if="filterBrand.length === 0 && !filterPrice && filterStorageSize.length === 0" 
        class="text-sm text-gray-400 italic"
      >
        No filters applied
      </span>
    </div>

    <!-- Combined Dropdown - Brand, Price และ Storage Size ในกรอบเดียวกัน -->
    <div
      v-if="isDropFilterBrand || isDropFilterPrice || isDropFilterStorageSize"
      class="w-auto bg-white border border-gray-300 p-4 shadow-md mt-2 rounded-lg max-h-64 overflow-y-auto"
    >
      <div class="flex space-x-6">
        
        <!-- Brand Section -->
        <div v-if="isDropFilterBrand" class="flex-1 min-w-48">
          <h3 class="text-sm font-semibold text-gray-700 mb-3 border-b pb-2">Brand</h3>
          <div class="flex flex-col space-y-2">
            <div
              v-for="brand in brands"
              :key="brand.id"
              class="flex items-center space-x-3 p-2 bg-gray-50 rounded border border-gray-200 hover:bg-gray-100 transition-all duration-200"
            >
              <input
                type="checkbox"
                :id="brand.name"
                :value="brand.name"
                v-model="filterBrand"
                class="h-4 w-4 text-gray-600 border-gray-300 rounded focus:ring-1 focus:ring-gray-300"
                @change="setFilterSortSaleItems(filterBrand, sortDirection, '')"
              />
              <label :for="brand.name" class="text-gray-700 text-sm cursor-pointer">
                {{ brand.name }}
              </label>
            </div>
          </div>
        </div>

        <!-- Price Section -->
        <div v-if="isDropFilterPrice" class="flex-1 min-w-48">
          <h3 class="text-sm font-semibold text-gray-700 mb-3 border-b pb-2">Price Range</h3>
          
          <!-- Predefined Price Ranges -->
          <div class="mb-4">
            <div class="flex flex-col space-y-2">
              <div
                v-for="range in priceRanges"
                :key="range.label"
                class="flex items-center justify-between p-2 bg-gray-50 rounded border border-gray-200 hover:bg-gray-100 cursor-pointer transition-all duration-200"
                :class="filterPrice && filterPrice.label === range.label ? 'border-gray-400 bg-gray-100' : ''"
                @click="selectPriceRange(range)"
              >
                <div class="flex items-center space-x-3">
                  <input
                    type="radio"
                    :id="range.label"
                    :checked="filterPrice && filterPrice.label === range.label"
                    class="h-4 w-4 text-gray-600 border-gray-300 focus:ring-1 focus:ring-gray-300"
                    readonly
                  />
                  <label :for="range.label" class="text-gray-700 text-sm cursor-pointer">
                    {{ range.min.toLocaleString() }}-{{ range.max.toLocaleString() }} Baht
                  </label>
                </div>
              </div>
            </div>
          </div>

          <!-- Custom Price Range -->
          <div class="border-t pt-3">
            <div class="flex items-center space-x-2 mb-2">
              <input
                v-model="customPriceMin"
                type="number"
                placeholder="Min Price"
                class="itbms-price-item-min flex-1 px-2 py-1 text-xs border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-gray-400"
              />
              <input
                v-model="customPriceMax"
                type="number"
                placeholder="Max Price"
                class="itbms-price-item-max flex-1 px-2 py-1 text-xs border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-gray-400"
              />
              <span class="text-xs text-gray-600">Baht</span>
            </div>
            <button
              @click="setCustomPriceRange"
              class="w-full px-3 py-1 text-xs bg-gray-600 text-white rounded hover:bg-gray-700 transition-all duration-200"
            >
              Apply
            </button>
          </div>
        </div>

        <!-- Storage Size Section - เปลี่ยนเป็นใช้ checkbox แทน radio -->
        <div v-if="isDropFilterStorageSize" class="flex-1 min-w-48">
          <div class="flex items-center justify-between mb-3 border-b pb-2">
            <h3 class="text-sm font-semibold text-gray-700">Storage Size</h3>
          </div>

          <div class="flex flex-col space-y-2">
            <div
              v-for="storage in mockStorageSizes"
              :key="storage.id"
              class="flex items-center space-x-3 p-2 bg-gray-50 rounded border border-gray-200 hover:bg-gray-100 transition-all duration-200"
            >
              <input
                type="checkbox"
                :id="`storage-${storage.id}+`"
                :value="storage.value"
                v-model="filterStorageSize"
                class="h-4 w-4 text-gray-600 border-gray-300 rounded focus:ring-1 focus:ring-gray-300"
                @change="onStorageSizeChange"
              />
              <label :for="`storage-${storage.id}`" class="text-gray-700 text-sm cursor-pointer">
                {{ storage.name }}
              </label>
            </div>
          </div>
        </div>
      
      </div>
    </div>
  </div>
</template>