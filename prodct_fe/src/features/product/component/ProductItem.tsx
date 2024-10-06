import React from "react";
import { useNavigate } from "react-router-dom";
import { ProductV1 } from "../type/productTypes";

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

  const handleEdit = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    event.stopPropagation();
    const newProduct = prompt("Edit product:", product.name);
    if (newProduct) {
      onEdit({ id: product.id, name: newProduct} as ProductV1);
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
      <td data-label="Price">{product.price}</td>
      <td data-label="Actions" className="right-align">
        <button className="mb-1" onClick={(event) => handleEdit(event)}>Edit</button>
        <button className="mb-1 ml-1 button-danger" onClick={(event) => handleDelete(event)}>Delete</button>
      </td>
    </tr>
  );
};

export default ProductItem;
