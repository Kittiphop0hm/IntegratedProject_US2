import { defineStore } from "pinia";
import { ref, computed } from "vue";

export const useUserStore = defineStore(
  "user",
  () => {
    // reactive state
    const id = ref();
    const name = ref("");
    const role = ref("");

    // getters (computed)
    const isSeller = computed(() => role.value.toUpperCase() === "SELLER");

    // actions
    function setUser(user) {
      console.log(user)
      id.value = user.id;
      role.value = user.role;
      console.log("Set user in store");
      console.log(id.value);
      console.log(role.value);
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
      storage: localStorage, 
      paths: ["id", "name" , "role"], 
    },
  }
);
