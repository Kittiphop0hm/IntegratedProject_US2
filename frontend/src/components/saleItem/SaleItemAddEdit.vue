<script setup>
import Navbar from "@/views/Navbar.vue";
import SaleItemDetailModel from "../model/SaleItemDetailModel.vue";
import { useBrandStore } from "../../stores/brands.js";
import { storeToRefs } from "pinia";
import { onMounted, ref } from "vue";
const { brands } = storeToRefs(useBrandStore());
const { getBrands } = useBrandStore();
onMounted(() => {
  brands.value = getBrands();
});
const saleItem = ref({
  brandId: "",
  model: "",
  price: "",
  description: "",
  ramGb: "",
  screenSizeInch: "",
  storageGb: "",
  color: "",
  quantity: "",
});
</script>
<template>
  {{ saleItem.brandId }}
  {{ saleItem.model }}
  {{ saleItem.price }}
  {{ saleItem.description }}
  {{ saleItem.ramGb }}
  {{ saleItem.screenSizeInch }}
  {{ saleItem.storageGb }}
  {{ saleItem.color }}
  {{ saleItem.quantity }}
  <Navbar />
  <!-- {{ brands ? brands: "Nothing in brandList" }} -->
  <form @submit.prevent="submitForm">
    <SaleItemDetailModel>
      <template #path>
        <span class="font-semibold">New Sale Item</span>
      </template>
      <template #brand>
        <select
          v-model="saleItem.brandId"
          id="brand"
          class="max-h-40 overflow-y-auto bg-gray-400 ml-32 border rounded-md px-2 py-1 w-70"
          :required="true"
        >
          <option disabled value="">-- Select a brand --</option>
          <option
            v-for="(brand, index) in brands"
            :key="index"
            class="flex"
            :value="brand.brandId"
          >
            {{ brand.brandName }}
          </option>
        </select>
      </template>
      <template #model>
        <input
          v-model.trim="saleItem.model"
          type="text"
          class="ml-32 border rounded-md px-2 py-1 w-70"
          placeholder="Model Name"
          :required="true"
        />
      </template>
      <template #price>
        <span class="text-white">Price: </span>

        <input
          v-model.number="saleItem.price"
          type="number"
          class="ml-35 border text-white rounded-md px-2 py-1 w-70"
          placeholder="Price"
          :required="true"
        />
      </template>
      <template #desc>
        <textarea
          v-model.trim="saleItem.description"
          type="text"
          class="mt-1 border rounded-md px-2 py-1 w-118"
          placeholder="Description"
          :required="true"
        ></textarea>
      </template>
      <template #ram>
        <input
          v-model.number="saleItem.ramGb"
          type="number"
          class="ml-35 border rounded-md px-2 py-1 w-70"
          placeholder="RAM"
        />
      </template>
      <template #screen>
        <input
          v-model.number="saleItem.screenSizeInch"
          type="number"
          class="ml-21 border rounded-md px-2 py-1 w-70"
          placeholder="Screen Size"
        />
      </template>
      <template #storage>
        <input
          v-model.number="saleItem.storageGb"
          type="number"
          class="ml-29 border rounded-md px-2 py-1 w-70"
          placeholder="Storage"
        />
      </template>
      <template #color>
        <input
          v-model.trim="saleItem.color"
          type="text"
          class="ml-35 border rounded-md px-2 py-1 w-70"
          placeholder="Color"
        />
      </template>
      <template #quantity>
        <input
          v-model.number="saleItem.quantity"
          type="number"
          class="ml-7 border rounded-md px-2 py-1 w-70"
          placeholder="Quantity"
          :required="true"
        />
      </template>
      <template #button1>
        <button type="submit" class="text-white">Add</button>
      </template>
      <template #button2>
        <button class="text-white">Cancel</button>
      </template>
    </SaleItemDetailModel>
  </form>
</template>
<style scoped></style>
