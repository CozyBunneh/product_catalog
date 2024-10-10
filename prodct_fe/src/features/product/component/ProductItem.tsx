import React from "react";
import { useNavigate } from "react-router-dom";
import { CreateProductV1, isProductV1, ProductV1 } from "../type/productTypes";
import ProductForm from "./ProductForm";

interface ProductItemProps {
  product: ProductV1;
  onEdit: (product: ProductV1) => void;
  onDelete: (id: string) => void;
}

const ProductItem: React.FC<ProductItemProps> = ({ product, onEdit, onDelete }) => {
  const navigate = useNavigate();
  const navigateToDetails = (id: string) => {
    navigate(`/${id}`);
  }

  const handleEdit = (product: CreateProductV1 | ProductV1) => {
    if (isProductV1(product)){
      onEdit(product);
    }
  };

  const handleDelete = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    event.stopPropagation();
    onDelete(product.id);
  };

  return (
    <tr onClick={() => navigateToDetails(product.id)}>
      <td data-label="Name">{product.name}</td>
      <td data-label="Category">{product.category}</td>
      <td data-label="Price">€{product.price}</td>
      <td data-label="Actions" className="right-align">
        <div></div>
        <div style={{display: "flex"}}>
          <ProductForm className="table-product-edit-button" id={product.id} onSubmit={handleEdit} />
          <div className="ml-1">
            <button className="button-danger" onClick={(event) => handleDelete(event)}>Delete</button>
          </div>
        </div>
      </td>
    </tr>
  );
};

export default ProductItem;
