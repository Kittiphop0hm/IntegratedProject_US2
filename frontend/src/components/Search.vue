<script setup>
import { ref, onMounted } from "vue";

const searchKeyword = ref('');

const emit = defineEmits(['search']);

// โหลด search keyword จาก localStorage
onMounted(() => {
  const savedSearch = localStorage.getItem("searchKeyword");
  if (savedSearch) {
    searchKeyword.value = savedSearch;
  }
});

// ฟังก์ชันค้นหา
const handleSearch = () => {
  const keyword = searchKeyword.value.trim();
  localStorage.setItem("searchKeyword", keyword);
  emit('search', keyword);
};

// ฟังก์ชัน Clear search
const handleClearSearch = () => {
  searchKeyword.value = '';
  localStorage.setItem("searchKeyword", '');
  emit('search', '');
};

// รองรับการกด Enter
const handleKeyPress = (event) => {
  if (event.key === 'Enter') {
    handleSearch();
  }
};
</script>

<template>
  <div class="mt-10 flex items-center justify-center w-full">
    <div class="flex items-center space-x-4 w-full max-w-4xl px-8">
      <h1 class="text-2xl font-bold text-gray-800 whitespace-nowrap">
        ITBMS Shop
      </h1>

      <div class="flex flex-1 space-x-2">
        <input
          v-model="searchKeyword"
          @keypress="handleKeyPress"
          type="text"
          placeholder="Search for products..."
          class="itbms-search-text px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 w-full"
        />
        <button 
          @click="handleSearch"
          class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
        >
          Search
        </button>
        <button 
          @click="handleClearSearch"
          class="itbms-search-clear-button px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600"
        >
          Clear
        </button>
      </div>

      <!-- Add Profile and Cart Icons -->
      <div class="flex space-x-4">
        <div class="cursor-pointer">
          <i class="fas fa-user text-gray-600 text-2xl"></i>
        </div>
        <div class="cursor-pointer">
          <i class="fas fa-shopping-cart text-gray-600 text-2xl"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>