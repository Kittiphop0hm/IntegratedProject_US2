<script setup>
import { ref } from "vue";

const emit = defineEmits(["sortSaleItemByBrand"]);
const sortDirection = ref("");

const setSort = (direction) => {
  sortDirection.value = direction;
  emit("sortSaleItemByBrand", direction);
};
</script>

<template>
  <div class="flex items-center space-x-2 justify-end w-full">
    <span class="text-sm font-medium text-gray-700">Sort Brand : </span>

    <div
      class="itbms-brand-sort flex border border-gray-300 rounded-lg overflow-hidden"
    >
      <button
        @click="setSort('')"
        class="itbms-brand-none px-3 py-2 hover:bg-blue-100 transition"
        :class="
          sortDirection === 'default'
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
        @click="setSort('asc')"
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
        @click="setSort('desc')"
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
</template>
