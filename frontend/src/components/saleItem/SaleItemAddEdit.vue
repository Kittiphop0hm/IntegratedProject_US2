<script setup>
import Navbar from "@/views/Navbar.vue";
import SaleItemDetailModel from "../model/SaleItemDetailModel.vue";
import { computed, onMounted, ref, watch, watchEffect } from "vue";
import { useRouter, useRoute } from "vue-router";
import {
  getItems,
  addItem,
  getItemById,
  editItem,
  addImage,
} from "../../libs/fetchUtil.js";
const router = useRouter();
const route = useRoute();
const brands = ref([]);
const validationMessages = ref({
  brand: "",
  model: "",
  price: "",
  description: "",
  ramGb: "",
  screenSizeInch: "",
  storageGb: "",
  color: "",
  quantity: "",
});

function validateField(field) {
  const value = saleItem.value[field];

  if (field === "brand") {
    validationMessages.value.brand = !saleItem.value.brand.id
      ? "Brand must be selected."
      : "";
  }

  if (field === "model") {
    validationMessages.value.model =
      value.length < 1 || value.length > 60
        ? "Model must be 1-60 characters long."
        : "";
  }

  if (field === "price") {
    validationMessages.value.price =
      value === "" || value < 0 || !Number.isInteger(value)
        ? "Price must be non-negative integer."
        : "";
  }

  if (field === "description") {
    validationMessages.value.description =
      value === "" || value.length < 1 || value.length > 16384
        ? "Description must be 1-16,384 characters long."
        : "";
  }

  if (field === "ramGb") {
    validationMessages.value.ramGb =
      saleItem.value.ramGb === null ||
      saleItem.value.ramGb === undefined ||
      saleItem.value.ramGb === ""
        ? ""
        : !Number.isInteger(saleItem.value.ramGb) || saleItem.value.ramGb <= 0
        ? "RAM size must be positive integer or not specified."
        : "";
  }

  if (field === "screenSizeInch") {
    validationMessages.value.screenSizeInch =
      value !== "" &&
      value !== null &&
      (value <= 0 || !/^\d+(\.\d{1,2})?$/.test(value))
        ? "Screen size must be positive number with at most 2 decimal points or not specified."
        : "";
  }

  if (field === "storageGb") {
    validationMessages.value.storageGb =
      saleItem.value.storageGb === null ||
      saleItem.value.storageGb === undefined ||
      saleItem.value.storageGb === ""
        ? ""
        : !Number.isInteger(saleItem.value.storageGb) ||
          saleItem.value.storageGb <= 0
        ? "Storage size must be positive integer or not specified."
        : "";
  }

  if (field === "color") {
    validationMessages.value.color =
      value && (value.length < 1 || value.length > 40)
        ? "Color must be 1-40 characters long or not specified."
        : "";
  }

  if (field === "quantity") {
    validationMessages.value.quantity =
      value !== "" && (!Number.isInteger(value) || value < 0)
        ? "Quantity must be non-negative integer."
        : "";
  }
}

function isFormValid() {
  const fields = [
    "brand",
    "model",
    "price",
    "description",
    "ramGb",
    "screenSizeInch",
    "storageGb",
    "color",
    "quantity",
  ];

  fields.forEach((field) => validateField(field));

  return Object.values(validationMessages.value).every((msg) => msg === "");
}

const initSaleItem = {
  brand: {
    id: "",
  },
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
console.log(saleItem.value);
const saleItemForchecking = ref({ ...initSaleItem });
const isEditMode = ref(false)
const isAddMode = ref(false)
onMounted(async () => {
  if (!route.params.id) {
    isAddMode.value = !isAddMode.value
    console.log("isAddMode:", isAddMode.value);
  } else {
    isEditMode.value = !isEditMode.value
    console.log("isEditMode:", isEditMode.value);  
  }
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
        // console.log("data:", data);
        console.log("rest:", rest);
        saleItem.value = { ...initSaleItem, ...rest };
        // console.log("Test First");
        // console.log("saleItem.value:", saleItem.value);
        saleItemForchecking.value = { ...initSaleItem, ...rest };
        // console.log("saleItemForchecking.value:", saleItemForchecking.value);
      }
    } catch (err) {
      console.error(err);
    }
    const brandsFilter = brands.value.find(
      (brand) => brand.name === saleItem.value.brandName
    );

    saleItem.value.brand.id = brandsFilter.id;
    saleItemForchecking.value.brand.id = brandsFilter.id;
  }
});

onMounted(async () => {
  if (isEditMode.value) {
    try {
      const items = await getItems(`${import.meta.env.VITE_APP_URL}/api/files/images`);
      items.forEach((item) => {
        const imageObj = {
          name: item,
          file: "",
        };
        images.value.push(imageObj)
      })
    } catch(err) {
      console.error(err);
    }
    
  }
})

const isSubmitted = ref(false);
const isUpdated = computed(() => {
   const checkUpdate = JSON.stringify(saleItem.value) !== JSON.stringify(saleItemForchecking.value)
   return checkUpdate || isNewImages.value
});

const previousPath = localStorage.getItem("previousPath");

const images = ref([]);
const imageFile = ref([])
const isImageFull = ref(false)

const showFilename = (e) => {
  const filenames = Array.from(e.target.files);
  filenames.forEach((file) => {
    const imageObj = {
      name: file.name,
      file: file,
    };
    if (images.value.length < 4) {
        images.value.push(imageObj);
        imageFile.value.push(file) 
    } else {
      isImageFull.value = true
      setTimeout(() => {
        isImageFull.value = false
      }, 3000)
    }
  });
    e.target.value = null
    console.log(images.value);
    console.log(imageFile.value);
};

const isNewImages = computed(() => images.value.length > 0)

const isActive = computed(() => {
  const isUpdatedField = 
      saleItem.value.brand.id !== "" &&
      saleItem.value.model !== "" &&
      saleItem.value.price !== "" &&
      saleItem.value.description !== "" &&
      isFormValid()
   return isUpdatedField || isNewImages.value
});

const deleteImg = (index) => {
  if (images.value.length > 0) {
    URL.revokeObjectURL(images.value[index].url)
    images.value.splice(index, 1);
  }
};

async function submitForm() {
  isSubmitted.value = true;
  if (!isFormValid()) {
    isSubmitted.value = false;
    return;
  }

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
      const item = await addItem(
        `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
        saleItem.value
      );
      console.log(item.data.id);
      await addImage(`${import.meta.env.VITE_APP_URL}/api/files`, imageFile.value, item.data.id)
      saleItem.value = { ...initSaleItem };
      router.push({ path: previousPath, query: { alertAdd: "true" } });
    } catch (error) {
      console.log(error);
    }
  }
}

const moveUp = (arr, index) => {
  const deleteElement = arr.splice(index, 1)[0]
  arr.splice(index - 1, 0, deleteElement)
  console.log(arr);
  
}

const moveDown = (arr, index) => {
  const deleteElement = arr.splice(index, 1)[0]
  arr.splice(index + 1, 0, deleteElement)
  console.log(arr);
}
// Edit ตอนลบแล้วคงความยาวไว้อาจจะต้องแอด String เปล่าเข้าไปแทนที่ตัวที่ลบ แล้วตอนแอดก็ค่อยแอดทับตัวที่เป็น string เปล่า
</script>

<template>
  <Navbar />
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
            <span class="font-semibold">{{ saleItem.model }}</span>

            <span class="font-semibold ml-1"
              >{{ saleItem.ramGb
              }}<span class="itbms-ramGb-unit">/GB </span></span
            >
            <span class="font-semibold">{{ saleItem.color }}</span>
          </router-link>
        </span>
      </template>
      <template #brand>
        <select
          v-model="saleItem.brand.id"
          id="brand"
          class="itbms-brand max-h-40 overflow-y-auto bg-gray-400 ml-32 border rounded-md px-2 py-1 w-70"
          @blur="validateField('brand')"
        >
          <option value="">-- Select a brand --</option>
          <option
            v-for="(brand, index) in brands"
            :key="index"
            class="flex"
            :value="brand.id"
            :required="true"
          >
            {{ brand.name }}
          </option>
        </select>
        <p
          v-if="validationMessages.brand"
          class="text-red-500 text-sm ml-32 itbms-message"
        >
          {{ validationMessages.brand }}
        </p>
      </template>
      <template #model>
        <input
          v-model.trim="saleItem.model"
          type="text"
          class="itbms-model ml-32 border rounded-md px-2 py-1 w-70"
          placeholder="Model Name"
          :required="true"
          @blur="validateField('model')"
        />
        <p
          v-if="validationMessages.model"
          class="text-red-500 text-sm ml-32 itbms-message"
        >
          {{ validationMessages.model }}
        </p>
      </template>
      <template #price>
        <span class="">Price: </span>

        <input
          v-model.number="saleItem.price"
          type="number"
          class="itbms-price ml-35 border rounded-md px-2 py-1 w-70"
          placeholder="Price"
          :required="true"
          @blur="validateField('price')"
        />
        <p
          v-if="validationMessages.price"
          class="text-red-500 text-sm ml-35 itbms-message"
        >
          {{ validationMessages.price }}
        </p>
      </template>
      <template #desc>
        <textarea
          v-model.trim="saleItem.description"
          type="text"
          class="itbms-description mt-1 border rounded-md px-2 py-1 w-118"
          placeholder="Description"
          :required="true"
          @blur="validateField('description')"
        ></textarea>
        <p
          v-if="validationMessages.description"
          class="text-red-500 text-sm mt-1 itbms-message"
        >
          {{ validationMessages.description }}
        </p>
      </template>
      <template #ram>
        <input
          v-model.number="saleItem.ramGb"
          type="number"
          class="itbms-ramGb ml-35 border rounded-md px-2 py-1 w-70"
          placeholder="RAM"
          @blur="validateField('ramGb')"
        />
        <p
          v-if="validationMessages.ramGb"
          class="text-red-500 text-sm ml-35 itbms-message"
        >
          {{ validationMessages.ramGb }}
        </p>
      </template>
      <template #screen>
        <input
          v-model.number="saleItem.screenSizeInch"
          type="number"
          step="0.01"
          class="itbms-screenSizeInch ml-21 border rounded-md px-2 py-1 w-70 itbms-message"
          placeholder="Screen Size"
          @blur="validateField('screenSizeInch')"
        />
        <p
          v-if="validationMessages.screenSizeInch"
          class="text-red-500 text-sm ml-21 itbms-message"
        >
          {{ validationMessages.screenSizeInch }}
        </p>
      </template>
      <template #storage>
        <input
          v-model.number="saleItem.storageGb"
          type="number"
          class="itbms-storageGb ml-29 border rounded-md px-2 py-1 w-70"
          placeholder="Storage"
          @blur="validateField('storageGb')"
        />
        <p
          v-if="validationMessages.storageGb"
          class="text-red-500 text-sm ml-29 itbms-message"
        >
          {{ validationMessages.storageGb }}
        </p>
      </template>
      <template #color>
        <input
          v-model.trim="saleItem.color"
          type="text"
          class="itbms-color ml-35 border rounded-md px-2 py-1 w-70"
          placeholder="Color"
          @blur="validateField('color')"
        />
        <p
          v-if="validationMessages.color"
          class="text-red-500 text-sm ml-35 itbms-message"
        >
          {{ validationMessages.color }}
        </p>
      </template>
      <template #quantity>
        <input
          v-model.number="saleItem.quantity"
          type="number"
          class="itbms-quantity ml-7 border rounded-md px-2 py-1 w-70"
          placeholder="Quantity"
          @blur="validateField('quantity')"
        />
        <p
          v-if="validationMessages.quantity"
          class="text-red-500 text-sm ml-7 itbms-message"
        >
          {{ validationMessages.quantity }}
        </p>
      </template>
      <template #button1>
        <button
          type="submit"
          :disabled="!isActive || !isUpdated || isSubmitted"
          class="itbms-save-button text-white rounded-md px-4 py-2"
        >
          Save
        </button>
      </template>
      <template #button2>
        <router-link :to="{ path: previousPath }">
          <button class="itbms-cancel-button text-white">Cancel</button>
        </router-link>
      </template>
    </SaleItemDetailModel>
  </form>
  <div class="w-fit ml-[51px]">
    <label
      for="file"
      class="cursor-pointer p-3 bg-amber-400 rounded-lg hover:opacity-80"
      >Upload picture</label
    >
    <input
      @change="showFilename"
      id="file"
      type="file"
      multiple
      class="cursor-pointer hidden"
    />
  </div>

    <div v-if="isImageFull" role="alert" class="alert alert-warning mx-[51px] my-5">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
      </svg>
      <span>Maximum 4 pictures are allowed.</span>
    </div>

    <div class="ml-[51px] my-4">
      <div class="flex flex-col space-x-4">
        <ul v-for="(img, index) in images" :key="index" class="flex">
          <li
            class="flex flex-col justify-center items-center bg-gray-200 my-1 py-2 px-2 rounded-lg relative"
          >
            <p class="text-[12px] p-2 font-semibold">{{ img.name }}</p>
            <button
              @click="deleteImg(index)"
              class="absolute flex justify
              -center items-center top-0 right-1 text-[12px] rounded-full font-black text-red-400 cursor-pointer hover:opacity-80"
            >
              X
            </button>
          </li>
          <div class="flex flex-col justify-center space-y-2 ml-2">
            <button @click="moveUp(images, index)" v-if="index > 0" class="bg-gray-300 rounded-full hover:bg-gray-200 cursor-pointer">
              <svg  xmlns="http://www.w3.org/2000/svg" width="25" height="25"  
                fill="currentColor" viewBox="0 0 24 24" >
                <path d="M13 18V9.91l3.29 3.3 1.42-1.42L12 6.09l-5.71 5.7 1.42 1.42L11 9.91V18z"></path>
              </svg>
            </button>

            <button @click="moveDown(images, index)" v-if="index < images.length -1" class="bg-gray-300 rounded-full hover:bg-gray-200 cursor-pointer">
              <svg  xmlns="http://www.w3.org/2000/svg" width="25" height="25"  
                fill="currentColor" viewBox="0 0 24 24" >
                <path d="M16.29 10.79 13 14.09V6h-2v8.09l-3.29-3.3-1.42 1.42 5.71 5.7 5.71-5.7z"></path>
              </svg>
            </button>
          </div>
        </ul>
      </div>

    <div class="space-x-1">
      <button
        @click="images.length = 0"
        v-if="images.length > 0"
        class="cursor-pointer py-2 px-3 bg-red-400 rounded-lg hover:opacity-80"
      >
        Clear
      </button>
    </div>
  </div>
</template>
<style scoped></style>
