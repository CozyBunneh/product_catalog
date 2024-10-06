import React, { useState } from "react";
import { CreateProductV1 } from "../type/productTypes";

interface ProductFormProps {
  onSubmit: (product: CreateProductV1) => void;
  style?: React.CSSProperties;
}

const ProductForm: React.FC<ProductFormProps> = ({ onSubmit, style }) => {
  const [name, setName] = useState<string>("");
  const [category, setCategory] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [price, setPrice] = useState<number>(0);
  const [imageUrl, setImageUrl] = useState<string>("");
  const [isOpen, setIsOpen] = useState(false);

  const openPopup = () => {
    setIsOpen(true);
  };

  const closePopup = () => {
    setIsOpen(false);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (name.trim()) {
      onSubmit({ name: name, category: category, description: description, price: price, imageUrl: imageUrl } as CreateProductV1);
      setName(""); // Clear the input after submission
      setCategory("");
      setDescription("");
      setPrice(0);
      setImageUrl("");
    }
    closePopup();
  };

  return (
    <div style={style}>
      <button onClick={openPopup}>Add Product</button>
      {isOpen && (
        <div className="popup">
          <form
            className="popup-content"
            onSubmit={handleSubmit}
          >
            <span className="close-button" onClick={closePopup}>&times;</span>
            <h2 style={{ marginTop: 0 }}>Create Product</h2>
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
