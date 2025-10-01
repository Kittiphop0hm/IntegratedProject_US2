import { defineStore } from "pinia";
import { ref, computed } from "vue";

export const useUserStore = defineStore(
  "user",
  () => {
    // reactive state
    const id = ref(null);
    const name = ref("");
    const role = ref("");

    // getters (computed)
    const isSeller = computed(() => role.value.toUpperCase() === "SELLER");

    // actions
    function setUser(user) {
      id.value = user.id;
      role.value = user.role;
    }

    function clearUser() {
      id.value = null;
      // name.value = "";
      role.value = "";
    }

    return {
      id,
      name,
      role,
      isSeller,
      setUser,
      clearUser,
    };
  },
  {
    persist: {
      storage: sessionStorage, 
      paths: ["id", "name" , "role"], 
    },
  }
);
