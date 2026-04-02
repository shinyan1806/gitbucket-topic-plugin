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

    // Autocomplete (topics list page)
    document.querySelectorAll('.topic-input').forEach(function (input) {
      initSuggest(input);
    });

    // Repository topic dropdown
    document.querySelectorAll('.topic-dropdown').forEach(function (container) {
      initDropdown(container);
    });

    // Autocomplete (shared between list page and dropdown)
    function initSuggest(input) {
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
    }

    function initDropdown(container) {
      var owner    = container.getAttribute('data-owner');
      var repo     = container.getAttribute('data-repo');
      var basePath = container.getAttribute('data-path') || '';

      var toggle    = container.querySelector('.topic-dropdown-toggle');
      var menu      = container.querySelector('.topic-dropdown-menu');
      var badgeList = container.querySelector('.topic-badge-list');
      var input     = container.querySelector('.topic-repo-input');
      var addBtn    = container.querySelector('.topic-add-btn');
      var errorDiv  = container.querySelector('.topic-error');
      var apiBase   = basePath + '/' + owner + '/' + repo + '/topics';

      input.setAttribute('data-path', basePath);
      initSuggest(input);

      toggle.addEventListener('click', function (e) {
        e.stopPropagation();
        if (menu.style.display === 'block') {
          menu.style.display = 'none';
        } else {
          menu.style.display = 'block';
          loadTopics();
        }
      });

      // Close on outside click
      document.addEventListener('click', function () {
        menu.style.display = 'none';
      });
      menu.addEventListener('click', function (e) {
        e.stopPropagation();
      });

      function loadTopics() {
        fetch(apiBase, { credentials: 'same-origin' })
          .then(function (r) { return r.json(); })
          .then(function (topics) { renderBadges(topics); })
          .catch(function () {
            errorDiv.textContent = 'Failed to load topics.';
            errorDiv.style.display = 'block';
          });
      }

      function renderBadges(topics) {
        badgeList.innerHTML = '';
        if (topics.length === 0) {
          var empty = document.createElement('span');
          empty.className = 'topic-no-topics';
          empty.textContent = 'No topics';
          badgeList.appendChild(empty);
          return;
        }
        topics.forEach(function (t) {
          var badge = document.createElement('span');
          badge.className = 'topic-badge';

          var label = document.createElement('span');
          label.textContent = t.name;

          var removeBtn = document.createElement('span');
          removeBtn.className = 'topic-badge-remove';
          removeBtn.textContent = '×';
          removeBtn.addEventListener('click', function () {
            removeTopic(t.name);
          });

          badge.appendChild(label);
          badge.appendChild(removeBtn);
          badgeList.appendChild(badge);
        });
      }

      addBtn.addEventListener('click', function () {
        var name = input.value.trim();
        if (!name) return;
        errorDiv.style.display = 'none';

        fetch(apiBase + '/add?topic=' + encodeURIComponent(name), {
          method: 'POST',
          credentials: 'same-origin'
        })
          .then(function (r) { return r.text(); })
          .then(function (text) {
            try {
              var topics = JSON.parse(text);
              input.value = '';
              renderBadges(topics);
            } catch (e) {
              errorDiv.textContent = 'Error: ' + text.substring(0, 200);
              errorDiv.style.display = 'block';
            }
          })
          .catch(function (e) {
            errorDiv.textContent = 'Network error: ' + e.message;
            errorDiv.style.display = 'block';
          });
      });

      function removeTopic(name) {
        fetch(apiBase + '/remove?topic=' + encodeURIComponent(name), {
          method: 'POST',
          credentials: 'same-origin'
        })
          .then(function (r) { return r.text(); })
          .then(function (text) {
            try {
              var topics = JSON.parse(text);
              renderBadges(topics);
            } catch (e) {
              errorDiv.textContent = 'Error: ' + text.substring(0, 200);
              errorDiv.style.display = 'block';
            }
          })
          .catch(function (e) {
            errorDiv.textContent = 'Network error: ' + e.message;
            errorDiv.style.display = 'block';
          });
      }
    }

  });

})();
