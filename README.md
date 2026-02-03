## Repository structure

This repository is organized as follows:

- `docs/` — Documentation and research artifacts  
  - `docs/papers/` — Paper PDFs (e.g., MODELSWARD, FSE Tool Paper, MoDIoT)  
  - `docs/tutorials/` — Tutorials and use-case walkthrough PDFs  
  - `docs/images/` — Images/screenshots used in documentation  

- `editors/` — Source code for the ML-QuadratPP editors  
  - `editors/textual/` — Textual editor(s) (Xtext / Eclipse / Maven)  
    - `ml2pp-textual/` — ML2++ textual editor  
    - `ml2-textual/` — ML2 textual editor (baseline / compatibility)  
    - `web/` — Web version of the textual editor (if applicable)  
  - `editors/graphical/` — Graphical editor (Sirius Web + Docker)  
    - `ml2pp-graphical/` — Dockerized graphical editor setup (`docker-compose.yml`, backend, web UI)  

- `examples/` — Example models and datasets  
  - `examples/textual/` — Textual examples  
  - `examples/graphical/` — Graphical examples  
  - `examples/datasets/` — Small CSV datasets used in example use cases  

- `scripts/` — Helper scripts (data preparation, utilities, etc.)

