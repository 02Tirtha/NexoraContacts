
  // table avtar
   const colors = [
        'bg-blue-500', 'bg-emerald-500', 'bg-rose-500', 'bg-fuchsia-500',
        'bg-yellow-500', 'bg-fuchsia-500', 'bg-orange-400', 'bg-teal-400'
        ];
let lastColorIndex = -1;

document.querySelectorAll('.avatar-circle').forEach((avatar, index) => {
  const name = avatar.textContent.trim();
    const color = colors[index % colors.length];
    avatar.classList.add(color);

//   //  Avoid same color as previous one
//   if (colorIndex === lastColorIndex) {
//     colorIndex = (colorIndex + 1) % colors.length;
//   }

//   const color = colors[colorIndex];
//   avatar.classList.add(color);
//   lastColorIndex = colorIndex;
});

function deleteContact(id) {
    Swal.fire({
        title: 'Are you sure?',
        text: 'Do you want to delete this contact?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, delete it!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            // First show "Deleted!" success message
            Swal.fire({
                title: 'Deleted!',
                text: 'The contact has been deleted.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });

            // Then redirect after short delay
            setTimeout(() => {
                window.location.href = `/user/contacts/delete/${id}`;
            }, 1500);
        }
    });
}
