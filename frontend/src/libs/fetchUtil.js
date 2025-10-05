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

async function getImage(url) {
  try {
    const data = await fetch(url);
    const item = await data.blob();
    return item;
  } catch (error) {
    if (data.status === 404) return undefined;
    throw new Error("can not get your item");
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
    const data = await fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`,
        Accept: "application/json"
      },
    });
    const items = await data.json();
    return items;
  } catch (error) {
    throw new Error("can not get your items");
  }
}

async function getItemsByIdWithToken(url, id, token) {
  try {
    const data = await fetch(`${url}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        Accept: "application/json"
      },
    });
    const items = await data.json();
    return items;
  } catch (error) {
    throw new Error("can not get your items");
  }
}

async function addSaleItemAndImageWithToken(url, item, images , token) {
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
    const data = await res.json();
    return {
      data: data,
      status: res.status,
    };
  } catch (err) {
    console.log(err);
  }
}
export {
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
  getItemsByIdWithToken
};
