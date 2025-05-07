<script setup>
import SaleItemGallery from '../saleItem/SaleItemGallery.vue';
import { ref, onMounted } from "vue";
import { getItems } from "../../libs/fetchUtil.js";
const items = ref([]);

onMounted(async () => {
  try {
    items.value = await getItems(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`
    );
  } catch (err) {
    console.log(err);
  }
});

</script>
<template>
  <SaleItemGallery :saleItems="items"></SaleItemGallery>
</template>
<style scoped></style>
