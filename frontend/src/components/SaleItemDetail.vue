<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { getItemById } from "../libs/fetchUtil";
import Navbar from "../views/Navbar.vue";

const route = useRoute();
const router = useRouter();
const item = ref(null);
const error = ref(null);

onMounted(async () => {
  try {
    const data = await getItemById(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
      route.params.id
    );
    if (data && data.id) {
      item.value = data;
    } else {
      error.value = "The requested item was not found.";
    }
  } catch (err) {
    console.error(err);
    error.value = "There was an error loading the item.";
  }
});

function goToHome() {
  router.push("/");
}
</script>

<template>
  <Navbar />

  <div class="mt-16 flex items-center justify-center w-full">
    <div class="flex items-center space-x-4 w-full max-w-4xl px-8">
      <h1 class="text-2xl font-bold text-gray-800 whitespace-nowrap">
        ITBMS Shop
      </h1>

      <div class="flex flex-1 space-x-2">
        <input
          type="text"
          placeholder="Search for products..."
          class="px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 w-full"
        />
        <button class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">
          Search
        </button>
      </div>

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

  <!-- Detail Section -->
  <div v-if="item" class="container mx-auto px-6 mt-10">
    <!-- Breadcrumb -->
    <div class="text-gray-800 bg-white bg-opacity-70 p-2 mb-4">
      <p>
        <span class="text-blue-600">Home</span> &gt;
        <span class="Itbms-mode font-semibold">{{ item.model }}</span>
        <span class="Itbms-ramGb font-semibold ml-1">{{ item.ramGb }}/GB </span>
        <span class="Itbms-color font-semibold">{{ item.color }}</span>
      </p>
    </div>

    
    <div class="flex flex-col lg:flex-row lg:space-x-10">
     
      <div class="lg:w-1/2">
        <div class=" rounded-md p-2 bg-white">
          <img src="/iPhone14ProMax.jpg" alt="main image" class="w-full " />
        </div>

        <div class="grid grid-cols-4 gap-2 mt-4">
          <div class="border border-gray-300 rounded p-1">
            <img src="/iPhone14ProMax.jpg" alt="thumb 1" class="w-30 mx-auto" />
          </div>
          <div class="border border-gray-300 rounded p-1">
            <img src="/iPhone14ProMax.jpg" alt="thumb 2" class="w-30 mx-auto" />
          </div>
          <div class="border border-gray-300 rounded p-1">
            <img src="/iPhone14ProMax.jpg" alt="thumb 3" class="w-30 mx-auto" />
          </div>
          <div class="border border-gray-300 rounded p-1">
            <img src="/iPhone14ProMax.jpg" alt="thumb 4" class="w-30 mx-auto" />
          </div>
        </div>
      </div>

    
      <div class="lg:w-1/2 mt-8 lg:mt-0">
        <p class="Itbms-brand text-lg mt-2">Brand: {{ item.brandName }}</p>
        <p class="Itbms-mode text-lg mt-2">Model: {{ item.model }}</p>
        <p class="Itbms-price text-lg text-blue-600 mt-2">
          Price: ฿{{ item.price }} Baht
        </p>
        <p class="Itbms-description text-lg mt-2">
          Description: <br />
          {{ item.description }}
        </p>
        <p class="Itbms-ramGb text-lg mt-2">RAM: {{ item.ramGb }} GB</p>
        <p class="Itbms-screenSizeInch text-lg mt-2">
          Screen size: {{ item.screenSizeInch }} inches
        </p>
        <p class="Itbms-storageGb text-lg mt-2">
          Storage: {{ item.storageGb }} GB
        </p>
        <p class="Itbms-color text-lg mt-2">Color: {{ item.color }}</p>
        <p class="Itbms-quantity text-lg mt-2">
          Available quantity: {{ item.quantity }} units
        </p>
      </div>
    </div>
  </div>

  <!-- Error Popup -->
  <div
    v-show="error"
    class="fixed top-10 left-1/2 transform -translate-x-1/2 z-50 bg-red-100 border border-red-500 text-red-700 px-6 py-4 rounded-md shadow-md transition-opacity duration-300"
  >
    <div class="flex items-center justify-between space-x-4">
      <p>{{ error }}</p>
      <button
        @click="goToHome"
        class="text-sm bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
      >
        Go Home
      </button>
    </div>
  </div>
</template>
