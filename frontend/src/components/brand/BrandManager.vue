<script setup>
import { onMounted, ref, computed } from "vue";
import { getItems, deleteItemById } from "@/libs/fetchUtil";
import Navbar from "@/views/Navbar.vue";
import Search from "../Search.vue";
import BrandList from "./BrandList.vue";
import { useRoute } from "vue-router";
import AlertMessageModel from "../model/AlertMessageModel.vue";
import { useRouter } from "vue-router";
import DeletePopupModel from "../model/DeletePopupModel.vue";
const router = useRouter();
const brands = ref([]);
const route = useRoute();
const data400 = ref();
onMounted(async () => {
  try {
    brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`);
    console.log(brands.value);
  } catch (error) {
    console.error("Error fetching brands:", error);
  }
});
const itemBrands = ref([]);
onMounted(async () => {
  try {
    const saleItems = await getItems(
      `${import.meta.env.VITE_APP_URL}/v1/sale-items`
    );
    itemBrands.value = saleItems.map((item) => item.brandName);
    itemBrands.value = [...new Set(itemBrands.value)];
  } catch (error) {
    console.error("Error fetching brands:", error);
  }
});

const fetchBrands = async () => {
  try {
    brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`);
  } catch (error) {
    console.error("Error fetching brands:", error);
  }
};

const isSuccess = computed(() => {
  if (
    route.query.alertAdd ||
    route.query.alertDelete ||
    route.query.alertUpdate
  )
    return true;
  if (route.query.alert404 || route.query.alertErrors) return false;
});

const alertMessage = computed(() => {
  if (route.query.alertAdd) return "added.";
  if (route.query.alertUpdate) return "updated.";
  if (route.query.alertDelete) return "deleted.";
  if (route.query.alert404)
    return "An error has occurred, the brand does not exist.";
  if (route.query.alertErrors) return "The brand could not be added";
  // if (route.query.alert400Delete)
  //   return (
  //     "Delete " +
  //     data400.value +
  //     " is not allowed. There are sale items with " +
  //     data400.value +
  //     " brand."
  //   );

  // `Delete ${data400.value} is not allowed. There are sale items with ${data400.value} brand.`;
  return "";
});

const getDataDeleted = (data) => {
  data400.value = data;
  console.log("infunction");
  console.log(data400.value);
};
console.log(data400.value);

const emit = defineEmits(["delete-success", "send-data"]);

const alertDeleteSuccess = ref(false);
const alertDeleteError = ref(false);

const confirmDelete = ref(false);
const selectedBrandId = ref("");
const selectedBrandName = ref("");

const deleteBrand = async () => {
  try {
    const res = await fetch(
      `${import.meta.env.VITE_APP_URL}/v1/brands/${selectedBrandId.value}`,
      { method: "DELETE" }
    );
    const removeIndex = brands.value.findIndex(
      (item) => item.id === selectedBrandId.value
    );
    brands.value.splice(removeIndex, 1);
    if (res.status === 204) {
      alertDeleteSuccess.value = true;
      router.push({ path: "/brands", query: { alertDelete: "true" } });
      // setTimeout(() => (alertDeleteSuccess.value = false), 5000);
    }
    if (res.status === 404) {
      alertDeleteError.value = true;
      router.push({ path: "/brands", query: { alert404: "true" } });
      // setTimeout(() => (alertDeleteError.value = false), 5000);
    }
    if (res.status === 400) {
      alertDeleteError.value = true;
      router.push({ path: "/brands", query: { alert400Delete: "true" } });
      // setTimeout(() => (alertDeleteError.value = false), 5000);
    }
  } catch (err) {
    console.error(error);
  } finally {
    confirmDelete.value = false;
  }
};

const isBrandInUse = ref(false);

const showDeleteConfirm = (item) => {
  selectedBrandId.value = item.id;
  selectedBrandName.value = item.name;
  if (itemBrands.value.includes(item.name)) {
    isBrandInUse.value = true;
  }
    console.log(selectedBrandName.value);
    confirmDelete.value = true;
    console.log("isBrandInUse.value: " + isBrandInUse.value)
    console.log("confirmDelete.value: " + confirmDelete.value)
};
const cancelDelete = () => {
  isBrandInUse.value = false;
  confirmDelete.value = false;
};

</script>

<template>
  <Navbar />
  <Search />

  <div
    v-show="
      route.query.alertAdd ||
      route.query.alertDelete ||
      route.query.alertUpdate ||
      route.query.alert404 ||
      route.query.alertErrors ||
      route.query.alert400Delete
    "
    class="p-10 pb-0"
  >
    <AlertMessageModel :isSuccess="isSuccess">
      <template #message>
        <p class="itbms-message" v-show="isSuccess === true">
          The brand has been
          <span class="text-green-400">{{ alertMessage }} </span>
        </p>
        <p class="itbms-message" v-show="isSuccess === false">
          {{ alertMessage }}
        </p>
      </template>
    </AlertMessageModel>
  </div>

  <div class="pt-5 px-10 mt-10">
    <div class="flex items-center">
      <router-link :to="{ name: 'BrandAdd' }" class="flex items-center">
        <button
          class="itbms-add-button px-4 py-2 bg-[#523F31] text-white rounded-md hover:opacity-80 cursor-pointer"
        >
          Add Brand
        </button>
      </router-link>
    </div>
  </div>
  <div class="pt-5">
    <BrandList
      :brands="brands"
      @delete-success="fetchBrands"
      @send-data="getDataDeleted"
      @deleteProduct="showDeleteConfirm"
    />

    <DeletePopupModel
      v-if="confirmDelete"
      @cancel-delete="cancelDelete"
      @delete-sale-item="deleteBrand"
      :canDelete="!isBrandInUse"
    >
      <template #message>
        <span class="itbms-message font-semibold" >
          <p v-show="isBrandInUse === false"> Do you want to delete {{ selectedBrandName }} brand? </p>
          <p v-show="isBrandInUse === true">
            Delete {{ selectedBrandName }} is not allowed. There are sale items
            with {{ selectedBrandName }} brand.
          </p>
        </span>
      </template>
    </DeletePopupModel>

    <!-- <NotAllowDeletePopup v-if="isBrandInUse" @cancel-delete="cancelDelete">
      <template #message>
        <span class="itbms-message font-semibold">
          Delete {{ selectedBrandName }} is not allowed. There are sale items
          with {{ selectedBrandName }} brand.
        </span>
      </template>
    </NotAllowDeletePopup> -->
  </div>
</template>
