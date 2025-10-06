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
        <div class="itbms-row relative border rounded-xl shadow hover:shadow-md p-4 transition duration-300 ease-in-out cursor-pointer">
          <img
            src="/images/iPhone14ProMax.jpg"
            alt="phone image"
            class="w-full h-36 object-cover rounded-md mb-4"
          />
          <div class="itbms-brand text-sm text-red-500">
            <!-- text-gray-900 -->
            {{ yourItem.brandName }}
          </div>
          <div class="text-lg font-semibold text-red-500 itbms-model">
            {{ yourItem.model }}
          </div>
          <div class="text-sm mt-1">
            <span class="itbms-ramGb">
              {{ yourItem.ramGb ? yourItem.ramGb : "-" }} /
            </span>
            <span class="itbms-storageGb">
              {{ yourItem.storageGb ? yourItem.storageGb : "-" }}
              <span class="itbms-storageGb-unit">GB</span></span
            >
          </div>
          <div class="text-lg font-bold mt-2 itbms-price">
            <span class="itbms-price-unit">Baht:</span
            >{{ yourItem.price.toLocaleString("en-US") }}
          </div>

          <!-- <button
            v-if="yourItem.isOwnedByCurrentUser"
            class="absolute bottom-3 right-3 bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:shadow-lg transition transform hover:-translate-y-1 active:translate-y-0"
          >
            Add to cart
          </button> -->
          
          <button
          @click.stop.prevent="checkRole(yourItem)"
            class="absolute bottom-3 right-3 bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-4 rounded-lg shadow-md hover:shadow-lg transition transform hover:-translate-y-1 active:translate-y-0"
          >
            Add to cart
          </button>
        </div>
      </router-link>
    </template>
  </SaleItemListModel>
</template>
