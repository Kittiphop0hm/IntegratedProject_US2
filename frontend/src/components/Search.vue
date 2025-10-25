<script setup>
import { ref, onMounted } from "vue";

const searchKeyword = ref('');

const emit = defineEmits(['search']);

// โหลด search keyword จาก sessionStorage
onMounted(() => {
  const savedSearch = sessionStorage.getItem("searchKeyword");
  if (savedSearch) {
    searchKeyword.value = savedSearch;
  }
});

// ฟังก์ชันค้นหา
const handleSearch = () => {
  const keyword = searchKeyword.value.trim();
  sessionStorage.setItem("searchKeyword", keyword);
  emit('search', keyword);
};

// ฟังก์ชัน Clear search
const handleClearSearch = () => {
  searchKeyword.value = '';
  sessionStorage.setItem("searchKeyword", '');
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
      <h1 class="text-2xl font-bold text-[#523F31] whitespace-nowrap">
        ITBMS Shop
      </h1>

      <div class="flex flex-1 space-x-2">
        <input
          v-model="searchKeyword"
          @keypress="handleKeyPress"
          type="text"
          placeholder="Search for products..."
          class="itbms-search-text px-4 py-2 border border-[#523F31] rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 w-full"
        />
        <button 
          @click="handleSearch"
          class="px-4 py-2 bg-[#523F31] text-white rounded-md cursor-pointer hover:opacity-90"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 32 32"><path fill="none" stroke="#ffffff" stroke-linecap="round" stroke-linejoin="round" stroke-width="4" d="m5 27l7.5-7.5M28 13a9 9 0 1 1-18 0a9 9 0 0 1 18 0Z"/></svg>
        </button>
        <button 
          @click="handleClearSearch"
          class="itbms-search-clear-button px-4 py-2 cursor-pointer bg-[#523F31] text-white font-semibold rounded-md hover:opacity-90"
        >
          Clear
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>