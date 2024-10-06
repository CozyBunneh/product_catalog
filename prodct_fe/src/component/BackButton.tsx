import React from "react";
import { useNavigate } from 'react-router-dom';

const BackButton: React.FC = () => {
  const navigate = useNavigate();

  const handleBack = () => {
    navigate(-1);
 };

  return (
    <button className="button-flat" onClick={handleBack} >
      &#8592; Back
    </button>
  );
};

export default BackButton;
