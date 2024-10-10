import React, { useEffect, useState } from "react";
import { CreateProductV1, ProductV1 } from "../type/productTypes";
import { fetchProductById } from "../service/productService";

interface ProductFormProps {
  id?: string | undefined;
  onSubmit: (product: CreateProductV1 | ProductV1) => void;
  style?: React.CSSProperties;
  className?: string | undefined;
}

const ProductForm: React.FC<ProductFormProps> = ({ id, onSubmit, style, className }) => {
  const [name, setName] = useState<string>("");
  const [category, setCategory] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [price, setPrice] = useState<number>(0);
  const [imageUrl, setImageUrl] = useState<string>("");
  const [isOpen, setIsOpen] = useState(false);
  const [fetchedProduct, setFetchedProduct] = useState<ProductV1 | null>(null);

  useEffect(() => {
    const loadProduct = async (id: string) => {
      if (id) {
        const product = await fetchProductById(id);
        setFetchedProduct(product);
      }
    };
    if (id && isOpen) {
      loadProduct(id);
    }
  }, [isOpen]);

  useEffect(() => {
    if (fetchedProduct) {
      setName(fetchedProduct.name);
      setCategory(fetchedProduct.category);
      setDescription(fetchedProduct.description);
      setPrice(fetchedProduct.price);
      setImageUrl(fetchedProduct.imageUrl);
    }
  }, [fetchedProduct]);


  const openPopup = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    event.stopPropagation();
    setIsOpen(true);
  };

  const closePopup = (event?: React.MouseEvent<HTMLSpanElement, MouseEvent>) => {
    if (event) {
      event.stopPropagation();
    }
    setIsOpen(false);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (name.trim()) {
      if (id) {
        onSubmit({ id: id, name: name, category: category, description: description, price: price, imageUrl: imageUrl } as ProductV1);
      } else {
        onSubmit({ name: name, category: category, description: description, price: price, imageUrl: imageUrl } as CreateProductV1);
      }
      setName(""); // Clear the input after submission
      setCategory("");
      setDescription("");
      setPrice(0);
      setImageUrl("");
    }
    closePopup();
  };

  return (
    <div className={className} style={style}>
      {id ? (
        <button onClick={(event) => openPopup(event)}>Edit</button>
      ) : (
        <button onClick={openPopup}>Add Product</button>
      )}
      {isOpen && (
        <div className="popup" onClick={(event) => closePopup(event)}>
          <form
            className="popup-content"
            onSubmit={handleSubmit}
            onClick={(event => event.stopPropagation())}
          >
            <span className="close-button" onClick={(event) => closePopup(event)}>&times;</span>
            {id ? (
              <h2 style={{ marginTop: 0 }}>Edit Product {id}</h2>
            ) : (
              <h2 style={{ marginTop: 0 }}>Create Product</h2>
            )}
            <h4 style={{ alignSelf: "start" }}>Name</h4>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter name"
            />
            <h4 style={{ alignSelf: "start" }}>Category</h4>
            <input
              type="text"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              placeholder="Enter category"
            />
            <h4 style={{ alignSelf: "start" }}>Description</h4>
            <input
              type="text"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Enter description"
            />
            <h4 style={{ alignSelf: "start" }}>Price</h4>
            <input
              type="number"
              value={price}
              onChange={(e) => setPrice(+e.target.value)}
              placeholder="Enter price"
            />
            <h4 style={{ alignSelf: "start" }}>Image URL</h4>
            <input
              type="text"
              value={imageUrl}
              onChange={(e) => setImageUrl(e.target.value)}
              placeholder="Enter image url"
            />
            <div className="mt-3" style={{ display: "flex", justifyContent: "flex-end" }}>
              <button className="button-danger" onClick={closePopup}>Close</button>
              <button className="ml-1" type="submit">Save</button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};

export default ProductForm;
