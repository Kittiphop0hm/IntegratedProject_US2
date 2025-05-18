success
<script setup>
import { onMounted, ref, computed } from "vue";
import { getItems } from "@/libs/fetchUtil";
import Navbar from "@/views/Navbar.vue";
import Search from "../Search.vue";
import BrandList from "./BrandList.vue";
import { useRoute } from "vue-router";
import AlertMessageModel from "../model/AlertMessageModel.vue";

const brands = ref([]);
const route = useRoute();
const data400 = ref();
onMounted(async () => {
  try {
    brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`);
  } catch (error) {
    console.error("Error fetching brands:", error);
  }
});

const fetchBrands = async () => {
  try {
    brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`);
  } catch (error) {
    console.error("Error fetching brands:", error);
  }
};

const isSuccess = computed(() => {
  if (
    route.query.alertAdd ||
    route.query.alertDelete ||
    route.query.alertUpdate
  )
    return true;
  if (
    route.query.alert404 ||
    route.query.alertErrors ||
    route.query.alert400Delete
  )
    return false;
});

const alertMessage = computed(() => {
  if (route.query.alertAdd) return "successfully added.";
  if (route.query.alertUpdate) return "updated.";
  if (route.query.alertDelete) return "deleted.";
  if (route.query.alert404) return "The brand could not be updated";
  if (route.query.alertErrors) return "The brand could not be added";
  if (route.query.alert400Delete)
    return (
      "Delete " +
      data400.value +
      " is not allowed. There are sale items with " +
      data400.value +
      " brand."
    );

  // `Delete ${data400.value} is not allowed. There are sale items with ${data400.value} brand.`;
  return "";
});

const getDataDeleted = (data) => {
  data400.value = data;
  console.log("infunction");
  console.log(data400.value);
};
console.log(data400.value);
</script>

<template>
  <Navbar />
  <Search />

  <div
    v-show="
      route.query.alertAdd ||
      route.query.alertDelete ||
      route.query.alertUpdate ||
      route.query.alert404 ||
      route.query.alertErrors ||
      route.query.alert400Delete
    "
    class="p-10 pb-0"
  >
    <AlertMessageModel :isSuccess="isSuccess">
      <template #message>
        <p class="itbms-message" v-show="isSuccess === true">
          The sale item has been
          <span class="text-green-400">{{ alertMessage }} </span>
        </p>
        <p class="itbms-message" v-show="isSuccess === false">
          {{ alertMessage }}
        </p>
      </template>
    </AlertMessageModel>
  </div>

  <div class="pt-5 px-10">
    <div class="flex items-center">
      <router-link
        to="/sale-items/list"
        class="itbms-item-list flex items-center mr-2 text-blue-500 hover:text-blue-300"
      >
        Sale item List
      </router-link>
      <router-link :to="{ name: 'BrandAdd' }" class="flex items-center">
        <button
          class="itbms-add-button ml-2 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 cursor-pointer"
        >
          Add Brand
        </button>
      </router-link>
    </div>
  </div>
  <div class="pt-5">
    <BrandList
      :brands="brands"
      @delete-success="fetchBrands"
      @send-data="getDataDeleted"
    />
  </div>
</template>
