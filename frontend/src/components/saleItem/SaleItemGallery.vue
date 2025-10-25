<script setup>
import { watch , onMounted , ref   } from "vue";
import SaleItemListModel from "../model/SaleItemListModel.vue";
import AlertMessageModel from "../model/AlertMessageModel.vue";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores/users";
const route = useRoute();
const props = defineProps({
  saleItems: {
    type: Array,
    required: true,
  },
});
console.log(props)
function savePreviousPath() {
  const previousPath = route.fullPath;
  localStorage.setItem("previousPath", previousPath);
}
const items = ref(props.saleItems);

    const emit = defineEmits(["addToCart"]);
function checkRole(yourItem) {
      console.log("test GALLERY")
  emit("addToCart", yourItem);
}



</script>

<template>
  <SaleItemListModel :items="saleItems">
    <template #listItem="{ yourItem }">
      <router-link
        :to="{ name: 'SaleItemDetail', params: { id: yourItem.id } }"
        class="block"
      >
        <div class="w-full itbms-row relative bg-[#9D8A7C] inset-shadow-sm inset-shadow-black rounded-xl shadow hover:-translate-y-1 active:translate-y-0 p-4 transition duration-300 ease-in-out cursor-pointer">
          <div class="w-full flex justify-center items-center">
          <img
            src="/images/carbon_no-image.png"
            alt="phone image"
            class="w-25 h-25 object-cover rounded-md mb-4"
          />
          </div>
          <div class="itbms-brand text-[#2D1E17] font-semibold">
            {{ yourItem.brandName }}
          </div>
          <div class="text-lg font-semibold text-[#2D1E17] itbms-model">
            {{ yourItem.model }}
          </div>
          <div class="text-sm mt-1">
            <span class="itbms-ramGb text-[#2D1E17]">
              {{ yourItem.ramGb ? yourItem.ramGb : "-" }} /
            </span>
            <span class="itbms-storageGb text-[#2D1E17]">
              {{ yourItem.storageGb ? yourItem.storageGb : "-" }}
              <span class="itbms-storageGb-unit">GB</span></span
            >
          </div>
          <div class="text-lg font-bold mt-2 itbms-price">
            <span class="itbms-price-unit">Baht:</span
            >{{ yourItem.price.toLocaleString("en-US") }}
          </div>
          
          <button
          @click.stop.prevent="checkRole(yourItem)"
            class="absolute bottom-3 right-3 bg-[#523F31] cursor-pointer hover:opacity-90 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:shadow-lg transition transform hover:-translate-y-1 active:translate-y-0"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 24 24"><path fill="#ffffff" d="M17 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2M1 2v2h2l3.6 7.59l-1.36 2.45c-.15.28-.24.61-.24.96a2 2 0 0 0 2 2h12v-2H7.42a.25.25 0 0 1-.25-.25c0-.05.01-.09.03-.12L8.1 13h7.45c.75 0 1.41-.42 1.75-1.03l3.58-6.47c.07-.16.12-.33.12-.5a1 1 0 0 0-1-1H5.21l-.94-2M7 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2Z"/></svg>
          </button>
        </div>
      </router-link>
    </template>
  </SaleItemListModel>
</template>
