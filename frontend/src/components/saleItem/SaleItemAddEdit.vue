<script setup>
import Navbar from "@/views/Navbar.vue";
import SaleItemDetailModel from "../model/SaleItemDetailModel.vue";
import { computed, onMounted, ref, watchEffect } from "vue";
import { useRouter, useRoute } from "vue-router";
import {
  getItems,
  addItem,
  getItemById,
  editItem,
} from "../../libs/fetchUtil.js";
const router = useRouter();
const route = useRoute();
const brands = ref([]);

const initSaleItem = {
  brandId: "",
  model: "",
  price: "",
  description: "",
  ramGb: "",
  screenSizeInch: "",
  storageGb: "",
  color: "",
  quantity: "",
};

const saleItem = ref({ ...initSaleItem });
const saleItemForchecking = ref({ ...initSaleItem });
onMounted(async () => {
  try {
    brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`);
  } catch (err) {
    console.log(err);
  }
  if (Number(route.params.id)) {
    try {
      const data = await getItemById(
        `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
        route.params.id
      );
      if (data.status === 404) {
        alert("The requested sale item does not exist.");
        router.back();
      } else {
        const { id, ...rest } = data;
        saleItem.value = { ...rest };
        // console.log("saleItem.value:", saleItem.value);
        saleItemForchecking.value = { ...rest };
        // console.log("saleItemForchecking.value:", saleItemForchecking.value);
      }
    } catch (err) {
      console.error(err);
    }
    const brandsFilter = brands.value.find(
      (brand) => brand.brandName === saleItem.value.brandName
    );
    saleItem.value.brandId = brandsFilter.brandId;
    saleItemForchecking.value.brandId = brandsFilter.brandId;
    console.log(JSON.stringify(saleItemForchecking.value));
    console.log(JSON.stringify(saleItem.value));
    console.log(Object.keys(saleItem.value));
  }
});

const isSubmitted = ref(false);
const isUpdated = computed(() => {
  // return (
  //   saleItem.value.brandId !== saleItemForchecking.value.brandId ||
  //   saleItem.value.model !== saleItemForchecking.value.model ||
  //   saleItem.value.price !== saleItemForchecking.value.price ||
  //   saleItem.value.description !== saleItemForchecking.value.description ||
  //   saleItem.value.ramGb !== saleItemForchecking.value.ramGb ||
  //   saleItem.value.screenSizeInch !== saleItemForchecking.value.screenSizeInch ||
  //   saleItem.value.storageGb !== saleItemForchecking.value.storageGb ||
  //   saleItem.value.color !== saleItemForchecking.value.color ||
  //   saleItem.value.quantity !== saleItemForchecking.value.quantity
  //  )
  return (
    JSON.stringify(saleItem.value) !== JSON.stringify(saleItemForchecking.value)
  );
});
// watchEffect(() => {
//   console.log('isUpdated:', isUpdated.value);
// });
const isActive = computed(() => {
  return (
    saleItem.value.brandId !== "" &&
    saleItem.value.model !== "" &&
    saleItem.value.price !== "" &&
    saleItem.value.description !== ""
  );
});

async function submitForm() {
  console.log(Object.keys(saleItem.value));
  isSubmitted.value = true;
  if (Number(route.params.id)) {
    await editItem(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
      route.params.id,
      saleItem.value
    );
    saleItem.value = { ...initSaleItem };
    router.push({
      name: "SaleItemDetail",
      params: { id: route.params.id },
      query: { alert: "true" },
    });
  } else {
    try {
      await addItem(
        `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
        saleItem.value
      );
      saleItem.value = { ...initSaleItem };
      router.push({ name: "SaleItemHome", query: { alertAdd: "true" } });
    } catch (error) {
      console.log(error);
    }
  }
}
</script>

<template>
  <!-- <br>
  {{ saleItem }}
  <br>
  <h1>---------------------------------------------------------------------------</h1>
  <br>
  {{ brands }}
  <h1>---------------------------------------------------------------------------</h1> -->

  <!-- <br>
  <h1>saleItemForchecking</h1> -->

  <!-- {{ saleItemForchecking }}
  <br>
  {{ saleItemForchecking.id }}
  {{ saleItemForchecking.brandId }}
  {{ saleItemForchecking.model }}
  {{ saleItemForchecking.price }}
  {{ saleItemForchecking.description }}
  {{ saleItemForchecking.ramGb }}
  {{ saleItemForchecking.screenSizeInch }}
  {{ saleItemForchecking.storageGb }}
  {{ saleItemForchecking.color }}
  {{ saleItemForchecking.quantity }} -->
  <!-- <h1>---------------------------------------------------------------------------</h1> -->
  <!-- <br>
  <br>
  <h1>saleItem</h1>
  {{ saleItem }}
  <br>
  <br>
  {{ saleItem.id }}
  {{ saleItem.brandId }}
  {{ saleItem.model }}
  {{ saleItem.price }}
  {{ saleItem.description }}
  {{ saleItem.ramGb }}
  {{ saleItem.screenSizeInch }}
  {{ saleItem.storageGb }}
  {{ saleItem.color }}
  {{ saleItem.quantity }} -->

  <Navbar />
  <!-- {{ brands ? brands: "Nothing in brandList" }} -->
  <form @submit.prevent="submitForm">
    <SaleItemDetailModel :isActive="isActive" :isUpdated="isUpdated">
      <template #path>
        <span class="font-semibold" v-show="!Number(route.params.id)"
          >New Sale Item
        </span>
        <span
          class="itbms-back-button font-semibold"
          v-if="Number(route.params.id)"
        >
        <router-link
            :to="{ name: 'SaleItemDetail', params: { id: route.params.id } }"
          >
          <span class=" font-semibold">{{ saleItem.model }}</span>

            <span class=" font-semibold ml-1"
              >{{ saleItem.ramGb
              }}<span class="itbms-ramGb-unit">/GB </span></span
            >
            <span class=" font-semibold">{{ saleItem.color }}</span>
          </router-link>
        </span>
      </template>
      <template #brand>
        <select
          v-model="saleItem.brandId"
          id="brand"
          class="itbms-brand max-h-40 overflow-y-auto bg-gray-400 ml-32 border rounded-md px-2 py-1 w-70"
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
          class="itbms-model ml-32 border rounded-md px-2 py-1 w-70"
          placeholder="Model Name"
          :required="true"
        />
      </template>
      <template #price>
        <span class="text-white">Price: </span>

        <input
          v-model.number="saleItem.price"
          type="number"
          class="itbms-price ml-35 border text-white rounded-md px-2 py-1 w-70"
          placeholder="Price"
          :required="true"
        />
      </template>
      <template #desc>
        <textarea
          v-model.trim="saleItem.description"
          type="text"
          class="itbms-description mt-1 border rounded-md px-2 py-1 w-118"
          placeholder="Description"
          :required="true"
        ></textarea>
      </template>
      <template #ram>
        <input
          v-model.number="saleItem.ramGb"
          type="number"
          class="itbms-ramGb ml-35 border rounded-md px-2 py-1 w-70"
          placeholder="RAM"
        />
      </template>
      <template #screen>
        <input
          v-model.number="saleItem.screenSizeInch"
          type="number"
          step="0.1"
          class="itbms-screenSizeInch ml-21 border rounded-md px-2 py-1 w-70"
          placeholder="Screen Size"
        />
      </template>
      <template #storage>
        <input
          v-model.number="saleItem.storageGb"
          type="number"
          class="itbms-storageGb ml-29 border rounded-md px-2 py-1 w-70"
          placeholder="Storage"
        />
      </template>
      <template #color>
        <input
          v-model.trim="saleItem.color"
          type="text"
          class="itbms-color ml-35 border rounded-md px-2 py-1 w-70"
          placeholder="Color"
        />
      </template>
      <template #quantity>
        <input
          v-model.number="saleItem.quantity"
          type="number"
          class="itbms-quantity ml-7 border rounded-md px-2 py-1 w-70"
          placeholder="Quantity"
          :required="true"
        />
      </template>
      <template #button1>
        <button
          type="submit"
          :disabled="!isActive || !isUpdated || isSubmitted"
          class="itbms-save-button text-white"
        >
          Save
        </button>
      </template>
      <template #button2>
        <router-link :to="{ name: 'SaleItemHome' }">
          <button class="itbms-cancel-button text-white">Cancel</button>
        </router-link>
      </template>
    </SaleItemDetailModel>
  </form>
</template>
<style scoped></style>
