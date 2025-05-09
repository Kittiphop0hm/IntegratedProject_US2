<script setup>

import SaleItemGallery from '../saleItem/SaleItemGallery.vue';
import { ref, onMounted } from "vue";
import { getItems } from "../../libs/fetchUtil.js";
import { useRoute } from 'vue-router';
const route = useRoute();
const saleItem = ref([]);

onMounted(async () => {
  try {
    saleItem.value = await getItems(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`
    );

  } catch (err) {
    console.log(err);
  }
});

</script>
<template>
  <div v-show="route.query.alert === 'true'" class=" p-10 pb-0">
    <div class="bg-black/20 shadow-xl rounded px-8 pt-6 pb-8">
      <h1 class=" text-2xl text-green-400">Successfully</h1>
      <br>
      <p> The Sale Item has been successfully added.</p>
    </div>
  </div>
    <div class="pl-10 pr-10 pt-10">
    <router-link :to="{ name: 'SaleItemAdd'  }">
      <button
        class="itbms-sale-item-add px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
      >
        Add Sale Item
      </button>
    </router-link>
  </div>
  <SaleItemGallery :saleItems="saleItem"></SaleItemGallery>
</template>
<style scoped></style>
