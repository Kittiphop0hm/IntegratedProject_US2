<script setup>
import ListTableModel from "../model/ListTableModel.vue";
import DeletePopupModel from "../model/DeletePopupModel.vue";
import { ref } from "vue";
import { useRouter } from "vue-router";
const router = useRouter()
const props = defineProps({
  brands: {
    type: Array,
    required: true,
  },
});

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
    if (res.status === 204) {
      alertDeleteSuccess.value = true;
      emit("delete-success");
      router.push({ path:'/brands', query: { alertDelete: "true" } });
      // setTimeout(() => (alertDeleteSuccess.value = false), 5000);
    }  
    if (res.status === 404) {
      alertDeleteError.value = true;
      router.push({ path:'/brands', query: { alert404: "true" } });
      // setTimeout(() => (alertDeleteError.value = false), 5000);
    }
    if (res.status === 400) {
      alertDeleteError.value = true;
      router.push({ path:'/brands', query: { alert400Delete: "true" } });
      // setTimeout(() => (alertDeleteError.value = false), 5000);
    }
  } catch (err) {
    console.error(error);
  } finally {
    confirmDelete.value = false;
  }
};
const showDeleteConfirm = (item) => {

  selectedBrandId.value = item.id;
  selectedBrandName.value = item.name;
  console.log(selectedBrandName.value);
  emit( "send-data" , selectedBrandName.value);
  confirmDelete.value = true;
};
const cancelDelete = () => {
  confirmDelete.value = false;
};
</script>

<template>
  <div class="px-10">
    <!-- <div v-show="alertDeleteSuccess || alertDeleteError" class="mb-5">
      <div class="bg-black/5 shadow-xl rounded px-8 pt-6 pb-8">
        <div v-show="alertDeleteSuccess">
          <h1 class="text-2xl text-green-400">Successfully</h1>
          <br />
          <p class="itbms-message">The brand has been deleted.</p>
        </div>
        <div v-show="alertDeleteError">
          <h1 class="text-2xl text-red-400">Error</h1>
          <br />
          <p class="itbms-message">The brand could not be deleted.</p>
        </div>
      </div>
    </div> -->

    <ListTableModel :items="brands">
      <template #listItem="{ yourItem }">
        <td class="border px-2 py-1 itbms-id">{{ yourItem.id ?? "-" }}</td>
        <td class="border px-2 py-1 itbms-brand">
          {{ yourItem.name ?? "-" }}
        </td>
      </template>

      <template #action="{ yourItem }">
        <div class="w-full py-2 flex justify-center">
          <router-link :to="{ name: 'BrandEdit', params: { id: yourItem.id } }">
            <button class="btn btn-info mr-2">
              <svg
                class="fill-current"
                xmlns="http://www.w3.org/2000/svg"
                width="25"
                height="32"
                viewBox="0 0 24 24"
              >
                <!-- Icon from Material Symbols by Google - https://github.com/google/material-design-icons/blob/master/LICENSE -->
                <path
                  d="M9 9V7h9v2zm0 3v-2h9v2zm3 8H5zm0 2H6q-1.25 0-2.125-.875T3 19v-3h3V2h15v9.025q-.5-.05-1.012.038t-.988.312V4H8v12h6l-2 2H5v1q0 .425.288.713T6 20h6zm2 0v-3.075l5.525-5.5q.225-.225.5-.325t.55-.1q.3 0 .575.113t.5.337l.925.925q.2.225.313.5t.112.55t-.1.563t-.325.512l-5.5 5.5zm7.5-6.575l-.925-.925zm-6 5.075h.95l3.025-3.05l-.45-.475l-.475-.45l-3.05 3.025zm3.525-3.525l-.475-.45l.925.925z"
                />
              </svg>
            </button>
          </router-link>
          <button
            class="btn btn-error ml-2 itbms-delete-button"
            @click="showDeleteConfirm(yourItem)"
          >
            <svg
              class="fill-current"
              xmlns="http://www.w3.org/2000/svg"
              width="25"
              height="32"
              viewBox="0 0 24 24"
            >
              <!-- Icon from Material Symbols by Google - https://github.com/google/material-design-icons/blob/master/LICENSE -->
              <path
                d="M7 6v13zm4.25 15H7q-.825 0-1.412-.587T5 19V6H4V4h5V3h6v1h5v2h-1v4.3q-.425-.125-.987-.213T17 10V6H7v13h3.3q.15.525.4 1.038t.55.962M9 17h1q0-1.575.5-2.588L11 13.4V8H9zm4-5.75q.425-.275.963-.55T15 10.3V8h-2zM17 22q-2.075 0-3.537-1.463T12 17t1.463-3.537T17 12t3.538 1.463T22 17t-1.463 3.538T17 22m1.65-2.65l.7-.7l-1.85-1.85V14h-1v3.2z"
              />
            </svg>
          </button>
        </div>

        <!-- <div
          v-show="confirmDelete"
          class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
        >
          <div class="bg-white p-6 rounded-lg shadow-lg w-96 text-center">
            <p class="text-lg mb-4 itbms-message">
              Do you want to delete "{{ selectedBrandName }}" brand?
            </p>
            <div class="flex justify-center gap-6">
              <button
                class="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 itbms-cancel-button"
                @click="confirmDelete = false"
              >
                Cancel
              </button>

              <button
                class="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 itms-confirm-button"
                @click="deleteBrand"
              >
                Confirm
              </button>
            </div>
          </div>
        </div> -->
      </template>
    </ListTableModel>
    <DeletePopupModel
      v-show="confirmDelete"
      @cancel-delete="cancelDelete"
      @delete-sale-item="deleteBrand"
    >
      <template #message>
        <span class="itbms-message font-semibold">
          Do you want to delete "{{ selectedBrandName }}" brand?
        </span>
      </template>
    </DeletePopupModel>
  </div>
</template>
