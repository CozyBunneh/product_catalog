export interface ProductV1 {
  id: string;
  name: string;
  category: string;
  description: string;
  price: number;
  imageUrl: string;
}

export function isProductV1(obj: any): obj is ProductV1 {
  return typeof obj === 'object' && obj !== null &&
    'id' in obj && 'name' in obj && 'category' in obj && 'description' in obj && 'price' in obj && 'imageUrl' in obj &&
    typeof obj.id === 'string' &&
    typeof obj.name === 'string' &&
    typeof obj.category === 'string' &&
    typeof obj.description === 'string' &&
    typeof obj.price === 'number' &&
    typeof obj.imageUrl === 'string';
}

export interface CreateProductV1 {
  name: string;
  category: string;
  description: string;
  price: number;
  imageUrl: string;
}

