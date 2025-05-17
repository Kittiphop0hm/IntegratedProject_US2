<script setup>
import ListTableModel from "../model/ListTableModel.vue";
import { ref } from "vue";
import { useRouter } from "vue-router";

const props = defineProps({
  brands: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["delete-success", "delete-error"]);

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
      setTimeout(() => (alertDeleteSuccess.value = false), 5000);
    } else {
      alertDeleteError.value = true;
      setTimeout(() => (alertDeleteError.value = false), 5000);
    }
  } catch (err) {
    alertDeleteError.value = true;
    setTimeout(() => (alertDeleteError.value = false), 5000);
  } finally {
    confirmDelete.value = false;
  }
};

const showDeleteConfirm = (item) => {
  selectedBrandId.value = item.id;
  selectedBrandName.value = item.name;
  confirmDelete.value = true;
};
</script>

<template>
  <div class="px-10">
    <div v-show="alertDeleteSuccess || alertDeleteError" class="mb-5">
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
    </div>

    <ListTableModel :items="brands">
      <template #listItem="{ yourItem }">
        <td class="border px-2 py-1 itbms-id">{{ yourItem.id ?? "-" }}</td>
        <td class="border px-2 py-1 itbms-brand">
          {{ yourItem.name ?? "-" }}
        </td>
      </template>

      <template #action="{ yourItem }">
        <router-link :to="{ name: 'BrandEdit', params: { id: yourItem.id } }">
          <button class="btn btn-info mr-2">Edit</button>
        </router-link>
        <button
          class="btn btn-error ml-2 itbms-delete-button"
          @click="showDeleteConfirm(yourItem)"
        >
          Delete
        </button>

        <div
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
        </div>
      </template>
    </ListTableModel>
  </div>
</template>
