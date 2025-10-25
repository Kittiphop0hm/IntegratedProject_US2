<script setup>
import Search from "../Search.vue";
import { ref, onMounted } from "vue";
import { getItems } from "@/libs/fetchUtil";
import { useRoute } from "vue-router";
import { useUserStore } from "../../stores/users.js";
const userStore = useUserStore();
const route = useRoute();
const emit = defineEmits(["fetchImagesForUpdate", "addToCart"]);

const props = defineProps({
  isActive: {
    type: Boolean,
    default: true,
  },
  isUpdated: {
    type: Boolean,
    default: true,
  },
  saleId: {
    type: Number,
  },
  isUpdateMode: {
    type: Boolean,
    default: false,
  },
  isAddMode: {
    type: Boolean,
    default: false,
  },
  saleItemEach: {
    type: Object,
    default: false,
  },
});

const pictures = ref([]);
const items = ref([]);
const mainImage = ref();
const quantityInCart = ref(1);
onMounted(async () => {
  try {
    if (route.params.id) {
      if (userStore.isSeller) {
        items.value = await getItems(
          `${import.meta.env.VITE_APP_URL}/v2/sale-items/${
            route.params.id
          }/sellers`
        );
      } else {
        items.value = await getItems(
          `${import.meta.env.VITE_APP_URL}/v2/sale-items/${route.params.id}`
        );
      }
      const saleItemImages = items.value.saleItemImages;
      emit("fetchImagesForUpdate", saleItemImages);
      if (saleItemImages && saleItemImages.length > 0) {
        saleItemImages.forEach((item) => {
          const apiFormat = `${import.meta.env.VITE_APP_URL}/api/files/${
            item.fileName
          }`;
          pictures.value.push(apiFormat);
        });
      } else {
        return;
      }
      mainImage.value = pictures.value[0];
    }
  } catch (err) {
    console.error(err);
  }
});

const clickShowImage = (index) => {
  mainImage.value = pictures.value[index];
};
function addQtyToParent(type) {
  console.log("addQtyToParent");
  if (quantityInCart.value > 1 && type === "decrease") {
    quantityInCart.value--;
        emit("addToCart", quantityInCart.value);
  }
  if (type === "increase") {
    if (quantityInCart.value + 1 <= items.value.quantity){
          console.log(items.value.quantity);
    quantityInCart.value++;
        emit("addToCart", quantityInCart.value);
    }
  }


}
</script>

<template>
  <Search />
  <div class="w-full min-h-screen flex justify-center items-center bg-white rounded-lg">
  <div class="w-[90%] rounded-2xl shadow-2xl p-6 bg-[#9D8A7C]">
  <div class="container bg-[#9D8A7C] mx-auto px-6 mt-10 mb-10 p-5 rounded-lg">
    <router-link :to="{name: 'SaleItemHome'}">
      <div class="w-20 h-15">
        <div class="w-full h-full flex justify-center items-center bg-[#523F31] shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] rounded-lg cursor-pointer hover:opacity-80">
          <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 17 16"><path fill="#ffffff" fill-rule="evenodd" d="m1.307 5.988l5.309-4.645c.411-.41.891-.479 1.302-.068v3.132l.229-.001c5.016 0 8.738 3.563 8.738 8.41c0 1.688-.774 1.073-1.097.484c-1.522-2.78-4.197-4.677-7.681-4.677l-.19.001v3.065c-.411.41-.941.361-1.302.068L1.306 7.474a1.052 1.052 0 0 1 .001-1.486z"/></svg>
        </div>
      </div>
    </router-link>
    <div class="w-[40%] max-lg:w-full bg-[#523F31] shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] rounded-lg font-semibold bg-opacity-70 p-4 mb-4 mt-7">
      <p class="text-white">
        <router-link :to="{ name: 'SaleItemHome' }">
          <span class="itbms-home-button text-white hover:underline">Home</span>
        </router-link>
        &gt;
        <span class="font-semibold text-white">
          <slot name="path"></slot>
        </span>
      </p>
    </div>

    <div class="itbms-row flex flex-col lg:flex-row lg:space-x-10">
      <div class="lg:w-1/2">
        <div class="w-[500px] h-[290px] max-md:w-full flex justify-center items-center relative rounded-md p-2 shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] bg-white">
          <img :src="!mainImage ? '/images/carbon_no-image-gray.png' : mainImage" alt="main image" class="w-50 h-50" />
        </div>

        <div class="w-[500px] flex justify-center items-center">
          <div class="w-full text-center grid grid-cols-4 gap-2 mt-4 max-md:grid-cols-2">
            <div
              @click="clickShowImage(0)"
              class="w-[100px] h-[100px] flex justify-center items-center relative shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] bg-[#796254] rounded p-1 hover:opacity-80 cursor-pointer"
            >
              <!-- <button class="absolute top-0 right-1 text-red-500 font-bold cursor-pointer hover:opacity-70">X</button> -->
              <img :src="!pictures[0] ? '/images/carbon_no-image-gray.png' : pictures[0]" alt="thumb 1" class="w-[74px] h-[74px] mx-auto" />
            </div>
            <div
              @click="clickShowImage(1)"
              class="w-[100px] h-[100px] flex justify-center items-center relative shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] bg-[#796254] rounded p-1 hover:opacity-80 cursor-pointer"
            >
              <!-- <button class="absolute top-0 right-1 text-red-500 font-bold cursor-pointer hover:opacity-70">X</button> -->
              <img :src="!pictures[1] ? '/images/carbon_no-image-gray.png' : pictures[1]" alt="thumb 2" class="w-[74px] h-[74px] mx-auto" />
            </div>
            <div
              @click="clickShowImage(2)"
              class="w-[100px] h-[100px] flex justify-center items-center relative shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] bg-[#796254] rounded p-1 hover:opacity-80 cursor-pointer"
            >
              <!-- <button class="absolute top-0 right-1 text-red-500 font-bold cursor-pointer hover:opacity-70">X</button> -->
              <img :src="!pictures[2] ? '/images/carbon_no-image-gray.png' : pictures[2]" alt="thumb 3" class="w-[74px] h-[74px] mx-auto" />
            </div>
            <div
              @click="clickShowImage(3)"
              class="w-[100px] h-[100px] flex justify-center items-center relative shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] bg-[#796254] rounded p-1 hover:opacity-80 cursor-pointer"
            >
              <!-- <button class="absolute top-0 right-1 text-red-500 font-bold cursor-pointer hover:opacity-70">X</button> -->
              <img :src="!pictures[4] ? '/images/carbon_no-image-gray.png' : pictures[4]" alt="thumb 4" class="w-[74px] h-[74px] mx-auto" />
            </div>
          </div>
        </div>
      </div>

      <div class="lg:w-1/2 mt-8 lg:mt-0">
        <p class="text-lg mt-2 text-white font-semibold">
          Brand:
          <span class="text-[#2D1E17]">
            <slot name="brand">Input Brand Name</slot>
          </span>
        </p>
        <p class="text-lg mt-2 text-white font-semibold">
          Model:
          <span class="text-[#2D1E17]">
            <slot name="model">input Model</slot>
          </span>
        </p>
        <p class="text-lg mt-2 text-white font-semibold">
          <span class="text-[#2D1E17]">
            <slot name="price">Input Price</slot>
          </span>
        </p>
        <p class="text-lg mt-2 text-white font-semibold">
          Description: <br />
          <span class="text-[#2D1E17]">
            <slot name="desc">Input Description</slot>
          </span>
        </p>
        <p class="text-lg mt-2 text-white font-semibold">
          RAM:
          <span class="text-[#2D1E17]">
            <slot name="ram">Input Ram and Unit</slot>
        </span>
        </p>
        <p class="text-lg mt-2 text-white font-semibold">
          Screen size:
          <span class="text-[#2D1E17]">
            <slot name="screen">Input Screen Size</slot>
        </span>
        </p>
        <p class="text-lg mt-2 text-white font-semibold">
          Storage:
          <span class="text-[#2D1E17]">
            <slot name="storage">Input Storage</slot>
        </span>
        </p>
        <p class="text-lg mt-2 text-white font-semibold">
          Color:
          <span class="text-[#2D1E17]">
            <slot name="color">Input Color</slot>
        </span>
        </p>
        <p class="text-lg mt-2 text-white font-semibold">
          Available quantity:
          <span class="text-[#2D1E17]">
            <slot name="quantity">Input Quantity</slot>
          </span>
        </p>
        <div class="gap-4 mt-3 flex">
          <div class="gap-4 flex" v-if="userStore.isSeller">
            <button
              class="px-[20px] py-[10px] shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] rounded-md text-white font-semibold"
              :class="{
                'bg-[#796254]': isActive && isUpdated,
                'bg-[#796254] opacity-75 cursor-not-allowed':
                  !isActive || !isUpdated,
              }"
              :disabled="!isActive || !isUpdated"
            >
              <slot name="button1">Input Name Button1</slot>
            </button>
            <button
              class="px-[20px] py-[10px] bg-[#8B3A3A] text-white font-semibold rounded-md hover:opacity-80 shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] cursor-pointer"
            >
              <slot name="button2">Input Name Button2</slot>
            </button>
          </div>

          <!-- <div v-if="items.seller">
            <button
            v-if="route.name === 'SaleItemDetail' && items.seller.id !== userStore.id"
            class="bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded-lg shadow-md hover:shadow-lg transition transform hover:-translate-y-1 active:translate-y-0"
          >
            <slot name="button3">Input Name Button3</slot>
          </button>
          </div> -->
          <div
            class="flex justify-center items-center gap-4 rounded-lg bg-[#796254] shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)]"
            v-if="route.name === 'SaleItemDetail'"
          >
            <button
              @click="addQtyToParent('decrease')"
              class="py-2 px-4 rounded-lg cursor-pointer hover:opacity-80"
            >
              <slot name="buttonMinus">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 48 48"><mask id="ipSReduceOne0"><g fill="none" stroke-linejoin="round" stroke-width="4"><path fill="#ffffff" stroke="#ffffff" d="M24 44c11.046 0 20-8.954 20-20S35.046 4 24 4S4 12.954 4 24s8.954 20 20 20Z"/><path stroke="#000000" stroke-linecap="round" d="M16 24h16"/></g></mask><path fill="#ffffff" d="M0 0h48v48H0z" mask="url(#ipSReduceOne0)"/></svg>
              </slot>
            </button>
            <button class="text-white font-semibold">
              <slot name="quantityInCart">Input Your Quantity</slot>
            </button>
            <button
              @click="addQtyToParent('increase')"
              class="py-2 px-4 rounded-lg cursor-pointer hover:opacity-80"
            >
              <slot name="buttonPlus">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 24 24"><path fill="#ffffff" d="M12 2C6.477 2 2 6.477 2 12s4.477 10 10 10s10-4.477 10-10S17.523 2 12 2zm5 11h-4v4h-2v-4H7v-2h4V7h2v4h4v2z"/></svg>
              </slot>
            </button>
          </div>
          <button
            v-if="route.name === 'SaleItemDetail'"
            class="w-[104px] h-[44px] flex justify-center items-center bg-[#523F31] shadow-black shadow-[0_4px_6px_rgba(0,4,4,0)] hover:opacity-80 cursor-pointer text-white py-[10px] px-[20px] rounded-lg hover:shadow-lg transition transform hover:-translate-y-1 active:translate-y-0"
          >
            <slot name="button3">Input Name Button3</slot>
          </button>
        </div>
      </div>
    </div>
  </div>
  </div>
  </div>
</template>
