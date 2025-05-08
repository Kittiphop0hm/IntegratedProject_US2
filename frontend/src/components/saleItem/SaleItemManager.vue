<script setup>

import SaleItemGallery from '../saleItem/SaleItemGallery.vue';
import { storeToRefs } from 'pinia';
import { useSaleItemStore } from '../../stores/saleItems.js';
import { ref, onMounted } from "vue";
const { saleItems } = storeToRefs(useSaleItemStore());
const { getSaleItems } = useSaleItemStore();

onMounted(async () => {
  saleItems.value = getSaleItems();
});

</script>
<template>
    <div class="pl-10 pr-10 pt-10">
    <router-link :to="{ name: 'SaleItemAdd' }">
      <button
        class="itbms-sale-item-add px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
      >
        Add Sale Item
      </button>
    </router-link>
  </div>
  <SaleItemGallery :saleItems="saleItems"></SaleItemGallery>
</template>
<style scoped></style>
