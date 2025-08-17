async function getItems(url) {
    try {
      const data = await fetch(url)
      const items = await data.json()
      return items
    } catch (error) {
      throw new Error('can not get your items')
    }
  }
  async function getItemById(url, id) {
    try {
      const data = await fetch(`${url}/${id}`)
      const item = await data.json()
      return item
    } catch (error) {
      if (data.status === 404) return undefined
      throw new Error('can not get your item')
    }
  }
  
  async function deleteItemById(url, id) {
    try {
      const res = await fetch(`${url}/${id}`, {
        method: 'DELETE'
      })
      return res.status
    } catch (error) {
      throw new Error('can not delete your item')
    }
  }

  async function deleteImageResource(url, filename) {
    try {
      const res = await fetch(`${url}/${filename}`, {
        method: 'DELETE'
      })
      return res.status
    } catch (error) {
      throw new Error('can not delete your image resource')
    }
  }
  
  async function addItem(url, newItem) {
    try {
      const res = await fetch(url, {
        method: 'POST',
        headers: {
          'content-type': 'application/json'
        },
        body: JSON.stringify({
          ...newItem
        })
      })
      const addedItem = await res.json()
      return {
        status: res.status,
        data: addedItem
      }
    } catch (error) {
      throw new Error('can not add your item')
    }
  }
  
  async function editItem(url, id, editItem) {
    try {
      const res = await fetch(`${url}/${id}`, {
        method: 'PUT',
        headers: {
          'content-type': 'application/json'
        },
        body: JSON.stringify({
          ...editItem
        })
      })
      const editedItem = await res.json()
      return {
        ...editedItem,
        status: res.status
      }
    } catch (error) {
      throw new Error('can not edit your item')
    }
  }

  async function addSaleItemAndImage(url, item, images) {
    try {
      const formdata = new FormData()
      formdata.append("model", item.model)
      formdata.append("description", item.description)
      formdata.append("price", item.price)
      formdata.append("ramGb", item.ramGb)
      formdata.append("screenSizeInch", item.screenSizeInch)
      formdata.append("quantity", item.quantity)
      formdata.append("storageGb", item.storageGb)
      formdata.append("color", item.color)
      formdata.append("brand.id", item.brand.id)
      formdata.append("brand.name", item.brand.name)
      if (images.length > 0) {
        for (let i = 0; i < images.length; i++) {
          formdata.append("images", images[i])  
        }
      }
      const res = await fetch(`${url}`, {
        method: "POST",
        body: formdata
      })
      const data = await res.json()
      return {
        data: data,
        status: res.status
      }
    } catch(err) {
      console.log(err);
    }
  }

  async function editSaleItemAndImage(url, id, item, images) {
    try {
      const formdata = new FormData()
      formdata.append("saleItem.model", item.model)
      formdata.append("saleItem.description", item.description)
      formdata.append("saleItem.price", item.price)
      formdata.append("saleItem.ramGb", item.ramGb)
      formdata.append("saleItem.screenSizeInch", item.screenSizeInch)
      formdata.append("saleItem.quantity", item.quantity)
      formdata.append("saleItem.storageGb", item.storageGb)
      formdata.append("saleItem.color", item.color)
      formdata.append("saleItem.brand.id", item.brand.id)
      formdata.append("saleItem.brand.name", item.brand.name)
      for (let i = 0; i < images.length; i++) {
        formdata.append("imageInfos.order", images[i].order)
        formdata.append("imageInfos.fileName", images[i].fileName)
        formdata.append("imageInfos.status", images[i].status)
        formdata.append("imageInfos.imageFile", images[i].imageFile)
      }
      const res = await fetch(`${url}/${id}`, {
        method: 'PUT',
        body: formdata
      })
      const data = await res.json()
      return {
        data,
        status: res.status
      }
    } catch(err) {
      console.log(err);
    }
  }

  async function imageUrlToFileObject(url, filename) {
    try {
      const res = await fetch(url)
      const blob = await res.blob()
      return new File([blob], filename, {type: blob.type})
    } catch(error) {
      throw new Error('can not convert image URL to file object')
    }
  }

    async function getIamgesBySaleId(url, saleId) {
    try {
      const data = await fetch(`${url}/${saleId}`)
      const item = await data.blob()
      return item
    } catch (error) {
      if (data.status === 404) return undefined
      throw new Error('can not get your item')
    }
  }
  export { imageUrlToFileObject, getItems, getItemById, deleteItemById, addItem, editItem, addSaleItemAndImage, getIamgesBySaleId, deleteImageResource, editSaleItemAndImage }