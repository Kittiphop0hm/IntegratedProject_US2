<script setup>
import SaleItemGallery from '../saleItem/SaleItemGallery.vue';
import { ref, onMounted } from "vue";
import { getItems } from "../../libs/fetchUtil.js";
import { useRoute } from 'vue-router';
import AlertMessageModel from '../model/AlertMessageModel.vue';
import FilterSaleItem from './FilterSaleItem.vue';

const route = useRoute();
const saleItem = ref([]);
const sortDirection = ref('default')

onMounted(async () => {
  try {
    saleItem.value = await getItems(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`
    );
  } catch (err) {
    console.log(err);
  }
});

function savePreviousPath() {
  const previousPath = route.fullPath;
  localStorage.setItem("previousPath", previousPath);
}
const isSuccess = ref(Boolean(route.query.alertAdd || route.query.alertDelete) && !route.query.alert404 );

// const filterSaleItemByBrand = async (filterBrand) => {
//   try {
//     if (!filterBrand || filterBrand.length === 0) {
//       saleItem.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`);
//     } else {
//     saleItem.value = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?filterBrands=${filterBrand}`)
//     }
//   } catch (err) {
//     console.log(err);
//   }
// }

// const sortSaleItemByBrand = async (direction) => {
//   try {
//     sortDirection.value = direction;

//     if (direction === 'default') {
     
//       saleItem.value = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items`);
//     } else {
    
//       saleItem.value = await getItems(
//         `${import.meta.env.VITE_APP_URL}/v2/sale-items?sortField=brand.name&sortDirection=${direction}`
//       );
//     }
//   } catch (err) {
//     console.log(err);
//   }
// }

const filterAndSortSaleItem = async(filterBrand, direction) => {
  console.log(filterBrand);
  console.log(!direction ? "empty" : "not empty");
  try {
    if (filterBrand || filterBrand.length >= 0) {
      if (direction === 'default') {
          saleItem.value = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?filterBrands=${filterBrand}`);
      } else {
          saleItem.value = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?filterBrands=${filterBrand}&sortField=brand.name&sortDirection=${direction}`);
      }
    } else {
      if (direction === 'default') {
          saleItem.value = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items`); 
      } else {
          saleItem.value = await getItems(`${import.meta.env.VITE_APP_URL}/v2/sale-items?sortField=brand.name&sortDirection=${direction}`); 
      }
    }
  } catch (err) {
    console.log(err);
  }
}


</script>
<template>
  <div v-show="route.query.alertAdd || route.query.alertDelete || route.query.alert404" class=" p-10 pb-0">
        <AlertMessageModel :isSuccess="isSuccess">
          <template #message>
            <p class="itbms-message" v-show="isSuccess === true"> The sale item has been <span class="text-green-400 "> {{ route.query.alertAdd ? 'successfully added.' : 'deleted.' }}</span></p>
            <p class="itbms-message" v-show="isSuccess === false">The requested sale item does not exist.</p>
          </template>
        </AlertMessageModel>
  </div>
    <div class="pl-10 pr-10 pt-10">
    <router-link :to="{ name: 'SaleItemAdd'  }">
      <button
      @click="savePreviousPath"
        class="itbms-sale-item-add px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">
        Add Sale Item
      </button>
    </router-link>
    <!-- <SortSaleItemByBrandname @sortSaleItemByBrand="filterAndSortSaleItem" /> -->
  </div>
  <FilterSaleItem @filterAndSortSaleItem="filterAndSortSaleItem"></FilterSaleItem>
  <SaleItemGallery :saleItems="saleItem"></SaleItemGallery>
</template>
<style scoped></style>
