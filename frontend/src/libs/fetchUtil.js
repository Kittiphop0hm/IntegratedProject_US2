import router from "@/router";
import { useCartStore } from "@/stores/carts";
import { useUserStore } from "@/stores/users";
import { includes } from "lodash";

async function getItems(url) {
  try {
    const data = await fetch(url);
    const items = await data.json();
    return items;
  } catch (error) {
    throw new Error("can not get your items");
  }
}

async function getItemById(url, id) {
  try {
    const data = await fetch(`${url}/${id}`);
    const item = await data.json();
    return item;
  } catch (error) {
    if (data.status === 404) return undefined;
    throw new Error("can not get your item");
  }
}

async function deleteItemById(url, id) {
  try {
    const res = await fetch(`${url}/${id}`, {
      method: "DELETE",
    });
    return res.status;
  } catch (error) {
    throw new Error("can not delete your item");
  }
}

async function deleteImageResource(url, filename) {
  try {
    const res = await fetch(`${url}/${filename}`, {
      method: "DELETE",
    });
    return res.status;
  } catch (error) {
    throw new Error("can not delete your image resource");
  }
}

async function addItem(url, newItem) {
  try {
    const res = await fetch(url, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...newItem,
      }),
      credentials: "include",
    });
    const addedItem = await res.json();
    return {
      status: res.status,
      data: addedItem,
    };
  } catch (error) {
    throw new Error("can not add your item");
  }
}

async function addItemNoBodyAndNoContent(url) {
  try {
    const res = await fetch(url, {
      method: "POST",
      credentials: "include",
    });
    return {
      status: res.status,
    };
  } catch (error) {
    throw new Error("can not add your item");
  }
}

async function addItemNoBody(url) {
  try {
    const res = await fetch(url, {
      method: "POST",
      credentials: "include",
    });
    const addedItem = await res.json();
    return {
      data: addedItem,
      status: res.status,
    };
  } catch (error) {
    throw new Error("can not add your item");
  }
}

async function editItem(url, id, editItem) {
  try {
    const res = await fetch(`${url}/${id}`, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...editItem,
      }),
    });
    const editedItem = await res.json();
    return {
      ...editedItem,
      status: res.status,
    };
  } catch (error) {
    throw new Error("can not edit your item");
  }
}

async function editForgotPassword(url, editItem) {
  try {
    const res = await fetch(url, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...editItem,
      }),
    });
    return res.status;
  } catch (error) {
    throw new Error("can not edit your item");
  }
}

async function addSaleItemAndImage(url, item, images) {
  try {
    const formdata = new FormData();
    formdata.append("model", item.model);
    formdata.append("description", item.description);
    formdata.append("price", item.price);
    formdata.append("ramGb", item.ramGb);
    formdata.append("screenSizeInch", item.screenSizeInch);
    formdata.append("quantity", item.quantity);
    formdata.append("storageGb", item.storageGb);
    formdata.append("color", item.color);
    formdata.append("brand.id", item.brand.id);
    formdata.append("brand.name", item.brand.name);
    if (images.length > 0) {
      for (let i = 0; i < images.length; i++) {
        formdata.append("images", images[i]);
      }
    }
    const res = await fetch(`${url}`, {
      method: "POST",
      body: formdata,
      credentials: "include",
    });
    const data = await res.json();
    return {
      data: data,
      status: res.status,
    };
  } catch (err) {
    console.log(err);
  }
}

async function editSaleItemAndImage(url, id, item, images) {
  try {
    const formdata = new FormData();
    formdata.append("saleItem.model", item.model);
    formdata.append("saleItem.description", item.description);
    formdata.append("saleItem.price", item.price);
    formdata.append("saleItem.ramGb", item.ramGb ?? "");
    formdata.append("saleItem.screenSizeInch", item.screenSizeInch ?? "");
    formdata.append("saleItem.quantity", item.quantity ?? "");
    formdata.append("saleItem.storageGb", item.storageGb ?? "");
    formdata.append("saleItem.color", item.color ?? "");
    formdata.append("saleItem.brand.id", item.brand.id);
    formdata.append("saleItem.brand.name", item.brand.name);
    for (let i = 0; i < images.length; i++) {
      if (images[i] !== "deleted") {
        formdata.append(`imageInfos[${i}].order`, images[i].order);
        formdata.append(`imageInfos[${i}].fileName`, images[i].fileName);
        formdata.append(`imageInfos[${i}].status`, images[i].status);
        formdata.append(`imageInfos[${i}].imageFile`, images[i].imageFile);
      }
    }
    const res = await fetch(`${url}/${id}`, {
      method: "PUT",
      body: formdata,
      credentials: "include",
    });
    const data = await res.json();
    return {
      data,
      status: res.status,
    };
  } catch (err) {
    console.log(err);
  }
}

async function imageUrlToFileObject(url, filename) {
  try {
    const res = await fetch(url);
    const blob = await res.blob();
    return new File([blob], filename, { type: blob.type });
  } catch (error) {
    throw new Error("can not convert image URL to file object");
  }
}

async function register(url, user, files) {
  try {
    const formdata = new FormData();
    if (user.role === "SELLER") {
      formdata.append("nickName", user.nickname);
      formdata.append("email", user.email);
      formdata.append("password", user.password);
      formdata.append("fullName", user.fullname);
      formdata.append("userType", user.role);
      formdata.append("phoneNumber", user.userPhone);
      formdata.append("bankAccount", user.bankAccount);
      formdata.append("bankName", user.bankName);
      formdata.append("cardNumber", user.idCardNumber);
      formdata.append("cardFrontImage", files[0]);
      formdata.append("cardBackImage", files[1]);
    } else {
      formdata.append("nickName", user.nickname);
      formdata.append("email", user.email);
      formdata.append("password", user.password);
      formdata.append("fullName", user.fullname);
      formdata.append("userType", user.role);
    }
    const res = await fetch(`${url}`, {
      method: "POST",
      body: formdata,
      credentials: "include",
    });
    const data = await res.json();
    return {
      data,
      status: res.status,
      error: res.statusText,
    };
  } catch (err) {
    console.log(err);
  }
}

async function verifyEmail(url) {
  try {
    const res = await fetch(url, {
      method: "POST",
      credentials: "include",
    });
    const items = await res.json();
    return {
      data: items,
      status: res.status,
    };
  } catch (error) {
    throw new Error("can not add your item");
  }
}

async function getItemsWithToken(url, token) {
  try {
    let response = await fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`,
        Accept: "application/json",
      },
    });

    if (response.status === 401) {
      const newAccessToken = await refreshAccessToken();
      response = await fetch(url, {
        headers: {
          Authorization: `Bearer ${newAccessToken}`,
          Accept: "application/json",
        },
      });
    }
    const items = await response.json();
    return items;
  } catch (error) {
    console.error(error);
    throw new Error("Cannot get your items");
  }
}
async function editSaleItemAndImageWithToken(url, id, item, images, token) {
  try {
    const formdata = new FormData();
    formdata.append("saleItem.model", item.model);
    formdata.append("saleItem.description", item.description);
    formdata.append("saleItem.price", item.price);
    formdata.append("saleItem.ramGb", item.ramGb ?? "");
    formdata.append("saleItem.screenSizeInch", item.screenSizeInch ?? "");
    formdata.append("saleItem.quantity", item.quantity ?? "");
    formdata.append("saleItem.storageGb", item.storageGb ?? "");
    formdata.append("saleItem.color", item.color ?? "");
    formdata.append("saleItem.brand.id", item.brand.id);
    formdata.append("saleItem.brand.name", item.brand.name);
    for (let i = 0; i < images.length; i++) {
      if (images[i] !== "deleted") {
        formdata.append(`imageInfos[${i}].order`, images[i].order);
        formdata.append(`imageInfos[${i}].fileName`, images[i].fileName);
        formdata.append(`imageInfos[${i}].status`, images[i].status);
        formdata.append(`imageInfos[${i}].imageFile`, images[i].imageFile);
      }
    }
    let res = await fetch(`${url}/${id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formdata,
      credentials: "include",
    });
    if (res.status === 401) {
      const newAccessToken = await refreshAccessToken();
      res = await fetch(`${url}/${id}`, {
        headers: {
          Authorization: `Bearer ${newAccessToken}`,
          Accept: "application/json",
        },
      });
    }
    const data = await res.json();
    return {
      data,
      status: res.status,
    };
  } catch (err) {
    console.log(err);
  }
}
async function addItemWithToken(url, newItem, token) {
  try {
    let res = await fetch(url, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "content-type": "application/json",
      },
      body: JSON.stringify(newItem),
    });
    if (res.status === 401) {
      const newAccessToken = await refreshAccessToken();
      res = await fetch(url, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${newAccessToken}`,
          "content-type": "application/json",
        },
        body: JSON.stringify(newItem),
      });
    }
    const addedItem = await res.json();
    return {
      status: res.status,
      data: addedItem,
    };
  } catch (error) {
    throw new Error("can not add your item");
  }
}
async function editItemWithToken(url, id, editItem, token) {
  try {
    let res = await fetch(`${url}/${id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...editItem,
      }),
    });
    if (res.status === 401) {
      const newAccessToken = await refreshAccessToken();
      res = await fetch(`${url}/${id}`, {
        headers: {
          Authorization: `Bearer ${newAccessToken}`,
          Accept: "application/json",
        },
      });
    }
    const editedItem = await res.json();
    return {
      ...editedItem,
      status: res.status,
    };
  } catch (error) {
    throw new Error("can not edit your item");
  }
}
async function deleteItemByIdWithToken(url, id, token) {
  try {
    let res = await fetch(`${url}/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    if (res.status === 401) {
      const newAccessToken = await refreshAccessToken();
      res = await fetch(`${url}/${id}`, {
        headers: {
          Authorization: `Bearer ${newAccessToken}`,
          Accept: "application/json",
        },
      });
    }
    return res.status;
  } catch (error) {
    throw new Error("can not delete your item");
  }
}

async function getItemsByIdWithToken(url, id, token) {
  try {
    let data = await fetch(`${url}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        Accept: "application/json",
      },
    });
    if (data.status === 401) {
      const newAccessToken = await refreshAccessToken();
      data = await fetch(`${url}/${id}`, {
        headers: {
          Authorization: `Bearer ${newAccessToken}`,
          Accept: "application/json",
        },
      });
    }
    const items = await data.json();
    return items;
  } catch (error) {
    throw new Error("can not get your items");
  }
}

async function addSaleItemAndImageWithToken(url, item, images, token) {
  try {
    const formdata = new FormData();
    formdata.append("model", item.model);
    formdata.append("description", item.description);
    formdata.append("price", item.price);
    formdata.append("ramGb", item.ramGb);
    formdata.append("screenSizeInch", item.screenSizeInch);
    formdata.append("quantity", item.quantity);
    formdata.append("storageGb", item.storageGb);
    formdata.append("color", item.color);
    formdata.append("brand.id", item.brand.id);
    formdata.append("brand.name", item.brand.name);
    if (images.length > 0) {
      for (let i = 0; i < images.length; i++) {
        formdata.append("images", images[i]);
      }
    }
    const res = await fetch(`${url}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      method: "POST",
      body: formdata,
      credentials: "include",
    });
    if (res.status === 401) {
      const newAccessToken = await refreshAccessToken();
      res = await fetch(`${url}`, {
        headers: {
          Authorization: `Bearer ${newAccessToken}`,
        },
        method: "POST",
        body: formdata,
        credentials: "include",
      });
    }
    const data = await res.json();
    return {
      data: data,
      status: res.status,
    };
  } catch (err) {
    console.log(err);
  }
}
async function refreshAccessToken() {
  try {
    console.log("refresh token called access Token");
    const response = await fetch(
      `${import.meta.env.VITE_APP_URL}/v2/auth/refresh`,
      {
        method: "POST",
        headers: {
          Accept: "application/json",
        },
        credentials: "include",
      }
    );
    if (!response.ok) {
      throw new Error("invalid Token");
    }
    const data = await response.json();
    const newToken = data?.access_token;
    localStorage.setItem("accessToken", newToken);
    return newToken;
  } catch (error) {
    const userStore = useUserStore();
    const cartStore = useCartStore();
    localStorage.removeItem("accessToken");
    localStorage.removeItem("nickname");
    userStore.clearUser();
    cartStore.clearCart();
    router.push("/signin");
  }
}

// const tokenStore = {
//   get() {
//     return localStorage.getItem("accessToken");
//   },
//   set(t) {
//     if (t) localStorage.setItem("accessToken", t);
//     else localStorage.removeItem("accessToken");
//   },
//   clear() {
//     localStorage.removeItem("accessToken");
//   },
// };

// single-flight refresh promise
// let refreshingPromise = null;

/**
 * refreshAccessToken: ปรับให้ throw เมื่อล้มเหลว เพื่อให้ caller รู้สถานะ
 * (ปกติคุณจะเรียก endpoint ที่ใช้ refresh token cookie, ดังนั้น credentials: 'include')
 */
// async function refreshAccessTokens() {
//   try {
//     console.log("[auth] refreshAccessToken called");
//     const response = await fetch(
//       `${import.meta.env.VITE_APP_URL}/v2/auth/refresh`,
//       {
//         method: "POST",
//         headers: { Accept: "application/json" },
//         credentials: "include",
//       }
//     );

//     if (!response.ok) {
//       const txt = await response.text().catch(() => "");
//       throw new Error(`refresh failed: ${response.status} ${txt}`);
//     }

//     const data = await response.json();
//     const newToken = data?.access_token;
//     if (!newToken) throw new Error("refresh returned no access token");

//     tokenStore.set(newToken);
//     console.log("[auth] refreshAccessToken success");
//     return newToken;
//   } catch (err) {
//     console.error("[auth] refreshAccessToken error:", err);
//     // ทำความสะอาดสถานะผู้ใช้ แล้ว throw เพื่อให้ caller (UI) รู้ว่า session หมด
//     try {
//       const userStore = useUserStore?.();
//       const cartStore = useCartStore?.();
//       if (userStore?.clearUser) userStore.clearUser();
//       if (cartStore?.clearCart) cartStore.clearCart();
//     } catch (e) {
//       // ignore store errors
//     }
//     tokenStore.clear();
//     // ถ้าต้องการ redirect ที่นี่ก็ได้ แต่แนะนำให้ caller จัดการ
//     // router.push("/signin");
//     throw err;
//   }
// }

/**
 * fetchWithAutoRefresh: wrapper สำหรับ request ที่ต้องใช้ access token.
 * - ถ้าได้ 401 ครั้งแรก จะเรียก refreshAccessToken (single-flight) แล้ว retry
 * - คืน response ให้ caller จัดการ (ยังไม่ parse body)
 */
// async function fetchWithAutoRefresh(input, init = {}) {
//   // helper สร้าง headers ที่รวม Authorization
//   const makeRequest = async (accessToken) => {
//     const headers = new Headers(init.headers || {});
//     if (accessToken) headers.set("Authorization", `Bearer ${accessToken}`);
//     // หากมี body เป็น JSON แต่ไม่ได้ตั้ง Content-Type ให้ตั้งให้
//     if (init.body && !headers.has("Content-Type")) {
//       headers.set("Content-Type", "application/json");
//     }
//     const merged = { ...init, headers };
//     return fetch(input, merged);
//   };

//   // 1) try initial request with current token
//   let token = tokenStore.get();
//   let res = await makeRequest(token);

//   if (res.status !== 401) return res;

//   // 2) got 401 -> refresh token (single-flight)
//   if (!refreshingPromise) {
//     refreshingPromise = (async () => {
//       try {
//         const newToken = await refreshAccessTokens(); // may throw
//         return newToken;
//       } finally {
//         refreshingPromise = null;
//       }
//     })();
//   }

//   // wait for refresh result (or throw)
//   const newToken = await refreshingPromise;

//   // 3) retry original request with new token
//   res = await makeRequest(newToken);
//   return res;
// }

/**
 * editItemWithToken: ใช้ fetchWithAutoRefresh เพื่อให้แน่ใจว่าจัดการ refresh และ retry ให้
 */
// async function editItemWithToken(url, id, editItem) {
//   try {
//     const endpoint = `${url}/${id}`;
//     const res = await fetchWithAutoRefresh(endpoint, {
//       method: "PUT",
//       headers: {
//         Accept: "application/json",
//       },
//       body: JSON.stringify(editItem),
//       // ถ้าต้องใช้ cookie (เช่น refresh token) ให้ใส่ credentials: 'include'
//       credentials: "include",
//     });

//     // อ่าน body อย่างปลอดภัย
//     const text = await res.text();
//     let parsed = null;
//     try {
//       parsed = text ? JSON.parse(text) : null;
//     } catch (e) {
//       parsed = text;
//     }

//     if (!res.ok) {
//       const msg =
//         parsed && parsed.message
//           ? parsed.message
//           : typeof parsed === "string"
//           ? parsed
//           : `HTTP ${res.status}`;
//       throw new Error(`Edit failed: ${msg}`);
//     }

//     return {
//       ...(parsed && typeof parsed === "object" ? parsed : { result: parsed }),
//       status: res.status,
//     };
//   } catch (error) {
//     console.error("[editItemWithToken] error:", error);
//     // ถ้าต้องการให้ UI เห็นข้อความชัด ให้โยน error ต้นฉบับ
//     throw error;
//   }
// }
export {
  editItemWithToken,
  addSaleItemAndImageWithToken,
  getItemsWithToken,
  verifyEmail,
  imageUrlToFileObject,
  getItems,
  getItemById,
  deleteItemById,
  addItem,
  editItem,
  addSaleItemAndImage,
  deleteImageResource,
  editSaleItemAndImage,
  register,
  addItemNoBody,
  addItemNoBodyAndNoContent,
  getItemsByIdWithToken,
  addItemWithToken,
  editForgotPassword,
  editSaleItemAndImageWithToken,
  deleteItemByIdWithToken,
};
