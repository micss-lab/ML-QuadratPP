# Editors

This folder contains the **ML-QuadratPP / ML2++ modeling editors**, provided in two complementary forms:

- **Textual editor** (Xtext / Eclipse / Maven) — for writing and validating models as text
- **Graphical editor** (Sirius Web + Docker) — for creating models visually in a web environment

## What is inside?

### Textual editor (`textual/`)
Xtext-based editors and related projects:

- **ML2++ textual editor**: [`textual/ml2pp-textual/`](textual/ml2pp-textual/)
- **ML2 textual editor (baseline/compatibility)**: [`textual/ml2-textual/`](textual/ml2-textual/)
- **Web textual editor (if applicable)**: [`textual/web/`](textual/web/)

➡️ See the tutorial: [Textual tutorial (PDF)](../docs/tutorials/textual_tutorial.pdf)

### Graphical editor (`graphical/`)
Sirius Web–based editor, packaged for deployment using Docker Compose:

- **ML2++ graphical editor**: [`graphical/ml2pp-graphical/`](graphical/ml2pp-graphical/)

➡️ See the tutorial: [Graphical tutorial (PDF)](../docs/tutorials/graphical_tutorial.pdf)

---

## Quick start

### A) Graphical editor (Docker)
Go to:
- [`graphical/ml2pp-graphical/`](graphical/ml2pp-graphical/)

Typical run command (if Docker is installed):
```bash
docker compose up --build
The exact ports and URLs depend on the configuration in docker-compose.yml.
Check the editor README inside graphical/ml2pp-graphical/ for the correct access URL.


If you want, I can also write:
- `editors/textual/README.md`
- `editors/graphical/README.md`

so each folder has a perfect quick-start page too.
