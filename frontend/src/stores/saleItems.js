import { ref, computed } from "vue";
import { defineStore, acceptHMRUpdate } from "pinia";
import { getItems , addItem } from "../libs/fetchUtil.js";
export const useSaleItemStore = defineStore("saleItems", () => {
  const saleItems = ref([]);
  const getSaleItems = async () => {
  try {
    saleItems.value = await getItems(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`
    );
  } catch (err) {
    console.log(err);
  }
  };
  const addSaleItemS = async (newItem) => {
    await addItem(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items/add`,
      newItem
    );
    await getSaleItems();
  }
  return { saleItems, getSaleItems , addSaleItemS };
});

if (import.meta.hot)
  import.meta.hot.accept(acceptHMRUpdate(useSaleItemStore, import.meta.hot));
