(function () {

  document.addEventListener('DOMContentLoaded', function () {

    // Topic filter dropdown
    var filterForm = document.querySelector('.topic-filter-form');
    if (filterForm) {
      var hiddenTopicInput = filterForm.querySelector('.topic-filter-hidden');
      filterForm.querySelectorAll('.topic-filter-option').forEach(function (link) {
        link.addEventListener('click', function (e) {
          e.preventDefault();
          hiddenTopicInput.value = link.getAttribute('data-topic') || '';
          filterForm.submit();
        });
      });
    }

    // Pencil button
    document.querySelectorAll('.topic-edit-btn').forEach(function (btn) {
      btn.addEventListener('click', function () {
        var owner = btn.getAttribute('data-owner');
        var name  = btn.getAttribute('data-name');
        var form  = document.getElementById('topic-edit-' + owner + '-' + name);
        if (form) {
          form.style.display =
            (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
        }
      });
    });

    // Autocomplete
    document.querySelectorAll('.topic-input').forEach(function (input) {
      var list     = input.parentElement.querySelector('.topic-suggest-list');
      var basePath = input.getAttribute('data-path') || '';
      var timer    = null;

      input.addEventListener('input', function () {
        clearTimeout(timer);
        var q = input.value.trim();
        if (q.length === 0) {
          hideList();
          return;
        }
        timer = setTimeout(function () {
          fetch(basePath + '/topics/search?q=' + encodeURIComponent(q), {
            credentials: 'same-origin'
          })
            .then(function (r) { return r.json(); })
            .then(function (names) { renderList(names); })
            .catch(function () { hideList(); });
        }, 200);
      });

      function renderList(names) {
        list.innerHTML = '';
        if (names.length === 0) {
          hideList();
          return;
        }
        names.forEach(function (name) {
          var item = document.createElement('li');
          item.className = 'topic-suggest-item';
          item.textContent = name;
          item.addEventListener('mousedown', function (e) {
            e.preventDefault();
            input.value = name;
            hideList();
          });
          list.appendChild(item);
        });
        list.style.display = 'block';
      }

      function hideList() {
        list.style.display = 'none';
        list.innerHTML = '';
      }

      input.addEventListener('blur', function () {
        setTimeout(hideList, 150);
      });
    });

  });

})();
