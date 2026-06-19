export default function Modal({ children, close }) {

  return (

    <div style={overlayStyle}>

      <div style={modalStyle}>

        <button
          style={closeStyle}
          onClick={close}
        >
          ✖
        </button>

        {children}

      </div>

    </div>

  );

}

const overlayStyle = {
  position: "fixed",
  top: 0,
  left: 0,
  width: "100%",
  height: "100%",
  background: "rgba(0,0,0,0.5)",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  zIndex: 1000
};

const modalStyle = {
  background: "white",
  padding: "25px",
  borderRadius: "8px",
  width: "600px",
  maxHeight: "80vh",
  overflowY: "auto"
};

const closeStyle = {
  float: "right",
  border: "none",
  background: "red",
  color: "white",
  padding: "5px 10px",
  cursor: "pointer"
};