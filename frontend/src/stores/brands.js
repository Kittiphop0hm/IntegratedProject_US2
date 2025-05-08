import { ref, computed } from 'vue'
import { defineStore, acceptHMRUpdate } from "pinia";
import { getItems } from "../libs/fetchUtil.js";
export const useBrandStore = defineStore("brands", () => {
  const brands = ref([]);
  const getBrands = async () => {
      try {
        brands.value = await getItems(
          `${import.meta.env.VITE_APP_URL}/v1/brands`
        );
      } catch (err) {
        console.log(err);
      }
  }
  return { brands , getBrands};
});

if (import.meta.hot)
  import.meta.hot.accept(acceptHMRUpdate(useBrandStore, import.meta.hot));