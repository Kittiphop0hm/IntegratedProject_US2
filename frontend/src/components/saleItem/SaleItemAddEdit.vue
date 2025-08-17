<script setup>
import Navbar from "@/views/Navbar.vue";
import SaleItemDetailModel from "../model/SaleItemDetailModel.vue";
import { computed, onMounted, ref, watch, watchEffect } from "vue";
import { useRouter, useRoute } from "vue-router";
import {
  getItems,
  getItemById,
  editItem,
  addSaleItemAndImage,
  deleteImageResource,
  imageUrlToFileObject,
  editSaleItemAndImage,
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
  model: "",
  description: "",
  price: "",
  ramGb: "",
  screenSizeInch: "",
  quantity: "",
  storageGb: "",
  color: "",
  brand: {
    id: "",
    name: "",
  },
};

const saleItem = ref({ ...initSaleItem });

const saleItemForchecking = ref({ ...initSaleItem });
const isEditMode = ref(false);
const isAddMode = ref(false);
onMounted(async () => {
  if (!route.params.id) {
    isAddMode.value = !isAddMode.value;
    console.log("isAddMode:", isAddMode.value);
  } else {
    isEditMode.value = !isEditMode.value;
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

const getBrandName = async (id) => {
  const brand = await getItemById(
    `${import.meta.env.VITE_APP_URL}/v1/brands`,
    id
  );
  initSaleItem.brand.name = brand.name;
  saleItem.value.brandName = brand.name;
  console.log(saleItem.value);
};

onMounted(async () => {
  if (isEditMode.value) {
    try {
      const items = await getItems(
        `${import.meta.env.VITE_APP_URL}/api/files/imageSale/${route.params.id}`
      );
      console.log(items);
      
      if (items.length > 0) {
        for (let i = 0; i < items.length; i++) {
          const imageUrlToObject = await imageUrlToFileObject(`${import.meta.env.VITE_APP_URL}/api/files/${items[i].fileName}`,items[i].fileName);
          const imageObj = {
            name: items[i].fileName,
            file: imageUrlToObject,
          };
          images.value.push(imageObj);
          imageFile.value.push(imageUrlToObject);
        }
      }
      console.log(images.value);
      
    } catch (err) {
      console.error(err);
    }
  }
});

const isSubmitted = ref(false);
const isUpdated = computed(() => {
  const checkUpdate =
    JSON.stringify(saleItem.value) !==
    JSON.stringify(saleItemForchecking.value);
  return checkUpdate || isUpdatedImages.value;
});

const previousPath = localStorage.getItem("previousPath");

const images = ref([]);
const imageFile = ref([]);
const isImageFull = ref(false);

const showFilename = (e) => {
  const filenames = Array.from(e.target.files);
  filenames.forEach((file) => {
    const imageObj = {
      name: file.name,
      file: file,
    };
    if (images.value.includes("deleted")) {
      const indexOfDeleted = images.value.indexOf("deleted");
      if (indexOfDeleted !== -1) {
        images.value[indexOfDeleted] = imageObj;
        imageFile.value[indexOfDeleted] = file;
      }
    } 
    else if (images.value.length < 4) {
      images.value.push(imageObj);
      imageFile.value.push(file);
    } 
    else {
      isImageFull.value = true;
      setTimeout(() => {
        isImageFull.value = false;
      }, 3000);
    }
  });
  e.target.value = null;
  console.log(images.value);
  console.log(imageFile.value);
};

const oldImages = ref([]);

const isUpdatedImages = computed(() => {
  const currentImages = images.value.filter(img => img !== "deleted")
  const previousImages = oldImages.value.filter(img => img.fileName !== "deleted")

  if (currentImages.length !== previousImages.length) return true;
  return currentImages.some(
    (img, index) => img.name !== previousImages[index].fileName
  )
})

const fetchImagesForupdate = async (items) => {
  oldImages.value = [...items];
}

const isActive = computed(() => {
  const isUpdatedField =
    saleItem.value.brand.id !== "" &&
    saleItem.value.model !== "" &&
    saleItem.value.price !== "" &&
    saleItem.value.description !== "" &&
    isFormValid();
  return isUpdatedField;
});

const imageReadyDeletes = ref([]);

const deleteImg = (index) => {
  if (index < 0 || index >= images.value.length) return;
  if (images.value.length > 0) {
      imageReadyDeletes.value.push(images.value[index]);
      images.value.splice(index, 1, "deleted");
      imageFile.value.splice(index, 1, "deleted");
    console.log(imageFile.value);
  }
};

async function submitForm() {
  isSubmitted.value = true;
  if (!isFormValid()) {
    isSubmitted.value = false;
    return;
  }
  if (Number(route.params.id)) {
      if (!isUpdatedImages.value) {
        await editItem(
          `${import.meta.env.VITE_APP_URL}/v1/sale-items`,
          route.params.id,
          saleItem.value
        );
        saleItem.value = { ...initSaleItem };
      }
      if (imageReadyDeletes.value.length > 0) {
        for (let i = 0; i < imageReadyDeletes.value.length; i++) {
            await deleteImageResource(`${import.meta.env.VITE_APP_URL}/api/files`,imageReadyDeletes.value[i].name);
        }
      }
      const saleItemImageObjectFormats = {
        model: saleItem.value.model,
        description: saleItem.value.description,
        price: saleItem.value.price,  
        ramGb: saleItem.value.ramGb,
        screenSizeInch: saleItem.value.screenSizeInch,
        quantity: saleItem.value.quantity,
        storageGb: saleItem.value.storageGb,  
        color: saleItem.value.color,
        brand: {
          id: saleItem.value.brand.id,
          name: saleItem.value.brand.name
        }
      }
      if (images.value.includes("deleted") || imageFile.value.includes("deleted")) {
        const indexOfDeleted = images.value.indexOf("deleted");
        images.value.splice(indexOfDeleted, 1)
        imageFile.value.splice(indexOfDeleted, 1);
      }
      const imagesForUpdate = ref([]);
      const imageObjectFormats = {
        order: 0,
        fileName: "",
        status: "NEW",
        imageFile: {},
      };
      imageFile.value.forEach((file, index) => {
        imageObjectFormats.order = index + 1;
        imageObjectFormats.fileName = file.name;
        imageObjectFormats.imageFile = file;
        imagesForUpdate.value.push({ ...imageObjectFormats });
      });
      console.log(images.value);
      console.log(imageFile.value);
      console.log(imagesForUpdate.value);
      
      const editSaleItem = await editSaleItemAndImage(
        `${import.meta.env.VITE_APP_URL}/v2/sale-items`,
        route.params.id,
        saleItemImageObjectFormats,
        imagesForUpdate.value
      );
      console.log(editSaleItem.data);
      
      saleItem.value = { ...initSaleItem };
      router.push({
      name: "SaleItemDetail",
      params: { id: route.params.id },
      query: { alert: "true" },
    });
  } else {
      if (images.value.includes("deleted") || imageFile.value.includes("deleted")) {
        const indexOfDeleted = images.value.indexOf("deleted");
        images.value.splice(indexOfDeleted, 1)
        imageFile.value.splice(indexOfDeleted, 1);
      }
    try {
      await addSaleItemAndImage(
        `${import.meta.env.VITE_APP_URL}/v2/sale-items`,
        saleItem.value,
        imageFile.value
      );
      saleItem.value = { ...initSaleItem };
      router.push({ path: previousPath, query: { alertAdd: "true" } });
    } catch (error) {
      console.log(error);
    }
  }
}

const moveUp = (arr, index) => {
  const deleteElement = arr.splice(index, 1)[0];
  arr.splice(index - 1, 0, deleteElement);
  const deleteImageFile = imageFile.value.splice(index, 1)[0];
  imageFile.value.splice(index - 1, 0, deleteImageFile);
  console.log("Image file: ", imageFile.value);
  console.log(arr);
};

const moveDown = (arr, index) => {
  const deleteElement = arr.splice(index, 1)[0];
  arr.splice(index + 1, 0, deleteElement);
  const deleteImageFile = imageFile.value.splice(index, 1)[0];
  imageFile.value.splice(index + 1, 0, deleteImageFile);
  console.log("Image file: ", imageFile.value);
  console.log(arr);
};

</script>

<template>
  <Navbar />
  <form @submit.prevent="submitForm">
    <SaleItemDetailModel
      :isActive="isActive"
      :isUpdated="isUpdated"
      @fetchImagesForUpdate="fetchImagesForupdate"
    >
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
          @change="getBrandName($event.target.value)"
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

  <div class="w-full space-y-6">
    <div class="w-full mx-auto max-w-4xl px-4 py-6 rounded-lg">
          <div class="mb-3 flex items-center justify-center">
            <label
              for="file"
              class="itbms-upload-button flex flex-col items-center justify-center w-full h-32 border-2 border-slate-300 border-dashed rounded-xl cursor-pointer bg-slate-50 hover:bg-slate-100 transition-all duration-200 group"
            >
              <div class="flex flex-col items-center justify-center pt-5 pb-6">
                <svg class="w-10 h-10 mb-3 text-slate-400 group-hover:text-slate-600 transition-colors duration-200" fill="none" stroke="currentColor" viewBox="0 0 48 48">
                  <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                <p class="mb-2 text-sm text-slate-500 group-hover:text-slate-600">
                  <span class="font-semibold">Click to upload</span> or drag and drop
                </p>
                <p class="text-xs text-slate-400">PNG, JPG, GIF up to 2MB</p>
              </div>
            </label>
            <input
              @change="showFilename"
              id="file"
              type="file"
              multiple
              class="hidden"
              accept="image/*"
            />
          </div>

          <div
            v-if="isImageFull"
            class="bg-amber-50 border-l-4 border-amber-400 p-4 rounded-lg animate-pulse"
            role="alert"
          >
            <div class="flex items-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 text-amber-400 mr-2"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
                />
              </svg>
              <span class="text-amber-700 font-medium">Maximum 4 pictures are allowed.</span>
            </div>
          </div>

          <div v-if="images.length > 0" class="space-y-3">
            <div
              v-for="(img, index) in images"
              :key="index"
              class="flex items-center justify-between bg-slate-50 border border-slate-200 rounded-lg p-4 group hover:bg-slate-100 transition-colors duration-200"
            >
              <div class="flex items-center space-x-4 flex-1">
                <div class="flex-shrink-0">
                  <div class="w-12 h-12 bg-slate-200 rounded-lg flex items-center justify-center">
                    <svg class="w-6 h-6 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                  </div>
                </div>
                
                <div class="flex-1 min-w-0">
                  <p :class="`itbms-picture-file${index+1} text-sm font-medium text-slate-700 truncate`">
                    {{ img.name }}
                  </p>
                  <p class="text-xs text-slate-500">Image {{ index + 1 }} of {{ images.length }}</p>
                </div>
              </div>

              <div class="flex items-center space-x-2">
                <button
                  @click="moveUp(images, index)"
                  :disabled="index === 0"
                  :class="`itbms-picture-file${index+1}-up p-2 rounded-lg transition-all duration-200 ${
                    index === 0 
                      ? 'text-slate-300 cursor-not-allowed' 
                      : 'text-slate-500 hover:text-slate-700 hover:bg-white'
                  }`"
                  title="Move up"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 15l7-7 7 7" />
                  </svg>
                </button>

                <button
                  @click="moveDown(images, index)"
                  :disabled="index === images.length - 1"
                  :class="`itbms-picture-file${index+1}-down p-2 rounded-lg transition-all duration-200 ${
                    index === images.length - 1 
                      ? 'text-slate-300 cursor-not-allowed' 
                      : 'text-slate-500 hover:text-slate-700 hover:bg-white'
                  }`"
                  title="Move down"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                  </svg>
                </button>

                <button
                  @click="deleteImg(index)"
                  :class="`itbms-picture-file${index+1}-clear p-2 rounded-lg text-red-400 hover:text-red-600 hover:bg-red-50 transition-all duration-200`"
                  title="Remove image"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
            </div>

            <div class="pt-4 border-t border-slate-200">
              <button
                @click="images.length = 0"
                class="inline-flex items-center px-4 py-2 text-sm font-medium text-red-600 bg-red-50 border border-red-200 rounded-lg hover:bg-red-100 hover:text-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 transition-all duration-200"
              >
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
                Clear All Images
              </button>
            </div>
          </div>
        </div>
      </div>
</template>
<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.itbms-message {
  animation: fadeIn 0.3s ease-out;
}

.itbms-brand::-webkit-scrollbar {
  width: 6px;
}

.itbms-brand::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 3px;
}

.itbms-brand::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.itbms-brand::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>