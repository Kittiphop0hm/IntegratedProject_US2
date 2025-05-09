import { ref, computed } from 'vue'
import { defineStore, acceptHMRUpdate } from "pinia";
export const useBrandStore = defineStore("brands", () => {
  const brandStore = ref([]);

  return { brandStore };
} , 
{
  persist: true
});

if (import.meta.hot)
  import.meta.hot.accept(acceptHMRUpdate(useBrandStore, import.meta.hot));