<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, onMounted, computed } from "vue";
import { getItemById } from "../../libs/fetchUtil";
import Navbar from "../../views/Navbar.vue";
import SaleItemDetailModel from "../model/SaleItemDetailModel.vue";

const route = useRoute();
const router = useRouter();
const item = ref([]);
onMounted(async () => {
  try {
    const data = await getItemById(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
      route.params.id
    );
    if (data.status === 404) {
      alert("The requested sale item does not exist.");
      router.push("/sale-items");
    } else {
      item.value = data;
    }
  } catch (err) {
    console.error(err);
  }
});

const formattedPrice = computed(() =>
  item.value?.price != null ? item.value.price.toLocaleString() : "-"
);
</script>

<template>
  <Navbar />
  <SaleItemDetailModel>
    <template #path>
      <span class="itbms-model font-semibold">{{ item.model }}</span>
      <span class="itbms-ramGb font-semibold ml-1"
        >{{ item.ramGb }}<span class="itbms-ramGb-unit">/GB </span></span
      >
      <span class="itbms-color font-semibold">{{ item.color }}</span>
    </template>
    <template #brand>{{ item.brandName }}</template>
    <template #model>{{ item.model }}</template>
    <template #price>Price: ฿ {{ formattedPrice }} Baht</template>
    <template #desc>{{ item.description }}</template>
    <template #ram
      >{{ item.ramGb ? item.ramGb : "-" }}
      <span class="itbms-ramGb-unit"> GB</span></template
    >
    <template #screen
      >{{ item.screenSizeInch ? item.screenSizeInch : "-" }}
      <span class="itbms-screenSizeInch-unit">Inches</span></template
    >
    <template #storage
      >{{ item.storageGb ? item.storageGb : "-" }}
      <span class="itbms-storageGb-unit">GB</span></template
    >
    <template #color>{{ item.color ? item.color : "-" }}</template>
    <template #quantity
      >{{ item.quantity }}
      <span class="itbms-quantity-unit">units</span></template
    >
        <template #button1>
      <span class="text-white">Edit</span>
    </template>
    <template #button2>
      <span class="text-white">Delete</span>
    </template>
  </SaleItemDetailModel>
</template>
